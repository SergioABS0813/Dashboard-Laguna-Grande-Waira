package pe.grande.laguna.dashboard.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.SparkmeterAlertsResponse;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.SparkmeterCustomerResponse;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Notification;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.NotificationRepository;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SparkMeterService {

    private static final String SPARKMETER_BASE_URL = "https://www.sparkmeter.cloud/api/v1";
    private final RestTemplate restTemplate = new RestTemplate();
    private final SettingsRepository settingsRepository;
    private final MicroNetworkRepository microNetworkRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final UsersRepository usersRepository;

    public SparkMeterService(SettingsRepository settingsRepository,
                             MicroNetworkRepository microNetworkRepository,
                             NotificationRepository notificationRepository, EmailService emailService,
                             UsersRepository usersRepository) {
        this.settingsRepository = settingsRepository;
        this.microNetworkRepository = microNetworkRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
        this.usersRepository = usersRepository;
    }

    public ResponseEntity<Map> getCustomersList(String siteId, String apiKey, String apiSecret) {
        String url = SPARKMETER_BASE_URL + "/customers";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-KEY", apiKey);
        headers.add("X-API-SECRET", apiSecret);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // Construimos la URL con parámetros
        String finalUrl = UriComponentsBuilder.fromHttpUrl(url)
                .toUriString();

        return restTemplate.exchange(finalUrl, HttpMethod.GET, request, Map.class);
    }

    // Falta implementar la funcion SCHEDULE para tareas periódicas para ver lo de los meters

    @Scheduled(fixedRate = 60000*5) // cada 5 minutos (300000 ms)
    public void checkSparkmeterAlertsPeriodically() {
        try {

            Instant now = Instant.now();
            Instant fiveMinutesAgo = now.minus(5, ChronoUnit.MINUTES);

            // Convertir a ZonedDateTime en Lima (UTC-5)
            ZonedDateTime limaNow = now.atZone(ZoneId.of("America/Lima"));
            ZonedDateTime limaFiveMinutesAgo = fiveMinutesAgo.atZone(ZoneId.of("America/Lima"));

            // Convertir las horas de Lima a UTC
            ZonedDateTime utcNow = limaNow.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime utcFiveMinutesAgo = limaFiveMinutesAgo.withZoneSameInstant(ZoneId.of("UTC"));

            // Formatear en ISO-8601 (para Sparkmeter)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String untilParam = formatter.format(utcNow);
            String sinceParam = formatter.format(utcFiveMinutesAgo);

            System.out.println("SINCE: " + sinceParam);
            System.out.println("UNTIL: " + untilParam);

            // Construir la URL con los parámetros
            String url = String.format(
                    "https://sparkmeter.cloud/api/v1/live-status/alerts?active_only=true&since=%s&until=%s",
                    sinceParam, untilParam
            );

            // Crear las cabeceras
            HttpHeaders headers = new HttpHeaders();
            List<Settings> listaSettings = settingsRepository.findAll();
            Settings settings = listaSettings.get(0); //Solo existe una configuración
            headers.set("X-API-KEY", settings.getKeySparkMeter());
            headers.set("X-API-SECRET", settings.getSecretSparkMeter());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Hacer la llamada
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();

                // Usar Jackson para parsear:
                ObjectMapper objectMapper = new ObjectMapper();
                SparkmeterAlertsResponse sparkResp =
                        objectMapper.readValue(responseBody, SparkmeterAlertsResponse.class);

                // Lista de AlertData
                List<SparkmeterAlertsResponse.AlertData> alerts = sparkResp.getData();

                if (alerts.isEmpty() || alerts.size() == 0){
                    System.out.println("No hay alertas en el sistema");
                }else {
                    //Significa que hay errores
                    for (SparkmeterAlertsResponse.AlertData alert : alerts) {
                        String timestamp = alert.getTimestamp();
                        // 1) Parseas como OffsetDateTime
                        OffsetDateTime odt = OffsetDateTime.parse(timestamp);
                        // 2) Convertimos a Instant
                        Instant instant = odt.toInstant();
                        // 3) Convertimos a Date
                        Date date = Date.from(instant);
                        System.out.println("DATE: " + date);
                        String type      = alert.getType();
                        String severity  = alert.getSeverity();
                        String idCustomer  = alert.getCustomer_id();

                        // Consultamos en base al id del Customer
                        String urlCustomer = "https://www.sparkmeter.cloud/api/v1/customers/" + idCustomer;

                        // Hacer la llamada
                        ResponseEntity<String> responseCustomer = restTemplate.exchange(
                                urlCustomer,
                                HttpMethod.GET,
                                requestEntity, // Ya tiene las cabeceras
                                String.class
                        );

                        if(responseCustomer.getStatusCode().is2xxSuccessful()){
                            String responseBodyCustomer = responseCustomer.getBody();

                            // Usar Jackson para parsear a objeto "SparkmeterCustomerResponse":
                            ObjectMapper objectMapperCustomer = new ObjectMapper();

                            SparkmeterCustomerResponse sparkRespCustomer =
                                    objectMapperCustomer.readValue(responseBodyCustomer, SparkmeterCustomerResponse.class);

                            String nombreCustomer = sparkRespCustomer.getData().getName();
                            String serviceAreaId = sparkRespCustomer.getData().getService_area_id();
                            String saldoCustomer = sparkRespCustomer.getData().getBalances().getCredit().getValue();
                            System.out.println(saldoCustomer);

                            // Hacer consulta para obtener el nombre del service_area
                            Optional<MicroNetwork> optMicronetwork = microNetworkRepository.findBySiteSparkMeter(serviceAreaId);
                            if (optMicronetwork.isPresent()){
                                MicroNetwork microNetworkChosen = optMicronetwork.get();
                                String nombreMicronetwork = microNetworkChosen.getAlias();
                                // - Crear notificacion y guardarla en DB
                                Notification notification = new Notification();
                                notification.setIdMicronetwork(microNetworkChosen.getId());
                                notification.setNameMicronetwork(nombreMicronetwork);
                                notification.setTimeCreation(date);
                                List<User> listaUsuariosEncargados = usersRepository.findByMicronetworkList(microNetworkChosen.getId());
                                // Hacer type y descripcion con un if:
                                if(type.equals("site_sync_delayed")){
                                    System.out.println(saldoCustomer);
                                    notification.setType("Sincronización del Sitio Retrasada");
                                    notification.setDescription("La sincronización del sitio está retrasada.");
                                    //Listar a las personas que están encargadas del service_area (siteSparkmeter)
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailSiteSyncDelayed(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("customer_low_balance")) {
                                    notification.setType("Saldo Bajo del Cliente");
                                    notification.setDescription("El saldo del cliente es muy bajo.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailCustomerLowBalance(user.getEmail(), microNetworkChosen.getAlias(), saldoCustomer, nombreCustomer);
                                    }

                                } else if (type.equals("customer_payment_pending")) {
                                    notification.setType("Pago del Cliente Pendiente");
                                    notification.setDescription("El pago del cliente está pendiente.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailCustomerPaymentPending(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("meter_state_discrepancy")) {
                                    notification.setType("Discrepancia en el Estado del Medidor");
                                    notification.setDescription("Existe una discrepancia en el estado del medidor.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterStateDiscrepancy(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }
                                } else if (type.equals("meter_state_protect_error")) {
                                    notification.setType("Error de Protección en el Estado del Medidor");
                                    notification.setDescription("Se ha detectado un error en la protección del medidor.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterStateProtectError(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("meter_state_tamper_error")) {
                                    notification.setType("Error de Manipulación en el Medidor");
                                    notification.setDescription("Se ha detectado una posible manipulación del medidor.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterStateTamperError(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("meter_state_throttle_error")) {
                                    notification.setType("Error de Limitación en el Medidor");
                                    notification.setDescription("Se ha detectado un error de limitación en el medidor.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterStateThrottleError(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("meter_sync_delayed")) {
                                    notification.setType("Sincronización del Medidor Retrasada");
                                    notification.setDescription("La sincronización del medidor está retrasada.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterSyncDelayed(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("meter_without_readings")) {
                                    notification.setType("Medidor sin Lecturas");
                                    notification.setDescription("El medidor no ha reportado lecturas recientemente.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailMeterWithoutReadings(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("customer_daily_energy_limit_reached ")) {
                                    notification.setType("Límite Diario de Energía Alcanzado");
                                    notification.setDescription("El cliente ha alcanzado el límite diario de energía.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailCustomerDailyEnergyLimitReached(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                } else if (type.equals("grid_monitor_sync_delayed ")) {
                                    notification.setType("Sincronización del Monitor de Red Retrasada");
                                    notification.setDescription("La sincronización del monitor de red está retrasada.");
                                    for (User user: listaUsuariosEncargados){
                                        emailService.sendAlertEmailGridMonitorSyncDelayed(user.getEmail(), microNetworkChosen.getAlias(), nombreCustomer);
                                    }

                                }else{
                                    System.out.println("No presentó ningún 'type'");
                                }
                                //Guardar Notification en DB:
                                notificationRepository.save(notification);

                            }else {
                                System.out.println("No se encontró ningún Micronetwork con el siteSparkmeter en DB: " + serviceAreaId);
                            }


                        }else {
                            System.err.println("Error al llamar a SparkMeter por Customer. Código: " + responseCustomer.getStatusCodeValue());

                        }

                    }

                }





            } else {
                System.err.println("Error al llamar a SparkMeter. Código: " + response.getStatusCodeValue());
            }

        } catch (Exception e) {
            System.err.println("Error consultando SparkMeter: " + e.getMessage());
            // Manejo de errores adicional
        }
    }


}
