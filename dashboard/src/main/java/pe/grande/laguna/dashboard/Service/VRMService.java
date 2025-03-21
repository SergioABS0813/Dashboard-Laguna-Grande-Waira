package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.VRMGraphResponse;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.VRMInstallationRecord;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.VRMInstallationsResponse;
import pe.grande.laguna.dashboard.Dto.VRMMeasurement;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Notification;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.NotificationRepository;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VRMService {

    private static final String VRM_BASE_URL = "https://vrmapi.victronenergy.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();
    private final EmailService emailService;
    private final SettingsRepository settingsRepository;
    private final MicroNetworkRepository microNetworkRepository;
    private final UsersRepository usersRepository;
    private final NotificationRepository notificationRepository;

    public VRMService(EmailService emailService, SettingsRepository settingsRepository,
                      MicroNetworkRepository microNetworkRepository,
                      UsersRepository usersRepository,
                      NotificationRepository notificationRepository) {
        this.emailService = emailService;
        this.settingsRepository = settingsRepository;
        this.microNetworkRepository = microNetworkRepository;
        this.usersRepository = usersRepository;
        this.notificationRepository = notificationRepository;
    }


    public ResponseEntity<Map> getMainData(String tokenVRM, String siteId, String interval, String start, String end) {
        // Construimos la URL base
        String baseUrl = VRM_BASE_URL + "/installations/" + siteId + "/stats";

        // Usamos UriComponentsBuilder para añadir los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("interval", interval)
                .queryParam("start", start)
                .queryParam("end", end)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET a la URL completa con los parámetros
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }


    public ResponseEntity<Map> getComponentsData(String tokenVRM, String siteId) {
        // Construimos la URL base
        String baseUrl = VRM_BASE_URL + "/installations/" + siteId + "/system-overview";

        // Usamos UriComponentsBuilder para añadir los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET a la URL completa con los parámetros
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }

    public ResponseEntity<Map> getDataAttributesData(String tokenVRM, String siteId) {
        // Construimos la URL base
        String baseUrl = VRM_BASE_URL + "/installations/" + siteId + "/attributes";

        // Usamos UriComponentsBuilder para añadir los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET a la URL completa con los parámetros
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }


    public ResponseEntity<Map> getDataWidget(String tokenVRM, String siteId, String attributeId, String instance, String start, String end) {
        // Construimos manualmente la URL sin usar UriComponentsBuilder para evitar la codificación de corchetes []
        String url = VRM_BASE_URL + "/installations/" + siteId + "/widgets/Graph" +
                "?attributeIds[]=" + attributeId +
                "&instance=" + instance +
                "&start=" + start +
                "&end=" + end;

        System.out.println("✅ URL final: " + url); // Verifica que la URL es la correcta

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET con la URL corregida
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }

    public List<Integer> obtenerIdSites(int idUser, String token) throws Exception {
        // Construir la URL
        // Por ejemplo: https://vrmapi.victronenergy.com/v2/users/543462/installations
        String url = String.format("https://vrmapi.victronenergy.com/v2/users/%d/installations", idUser);

        // Cabeceras (si se requiere token Token, x-authorization, etc.)
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-authorization", "Token " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear request entity con las cabeceras
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Llamar al endpoint
        ResponseEntity<VRMInstallationsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                VRMInstallationsResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            VRMInstallationsResponse body = response.getBody();
            if (body != null && body.isSuccess()) {
                // Extraer la lista de idSite
                return body.getRecords()
                        .stream()
                        .map(record -> record.getIdSite())
                        .collect(Collectors.toList());
            } else {
                throw new Exception("La respuesta indica success=false o body nulo.");
            }
        } else {
            throw new Exception("Error al llamar al endpoint: " + response.getStatusCode());
        }
    }

    public VRMGraphResponse obtenerGraphData(Integer siteId, long start, long end, String token) throws Exception {

        // 1. Construir la URL, p.ej.:
        //    https://vrmapi.victronenergy.com/v2/installations/{siteId}/widgets/Graph
        //    ?attributeCodes[]=bv
        //    &instance=0
        //    &start={start}
        //    &end={end}

        String url = "https://vrmapi.victronenergy.com/v2/installations/" + siteId + "/widgets/Graph" + "?attributeCodes[]=bv&instance=0&start=" + start + "&end=" + end  ;

        // 2. Cabeceras
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-authorization", "Token " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 3. RequestEntity
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // 4. Llamar a la API
        ResponseEntity<VRMGraphResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                VRMGraphResponse.class
        );

        // 5. Validar la respuesta
        if (response.getStatusCode().is2xxSuccessful()) {
            VRMGraphResponse body = response.getBody();
            if (body != null) {
                return body;
            } else {
                throw new Exception("Respuesta nula al consultar Graph en VRM.");
            }
        } else {
            throw new Exception("Error al llamar al endpoint VRM Graph: " + response.getStatusCode());
        }
    }

    public VRMMeasurement parseLastVoltage(VRMGraphResponse response, Integer siteId) {
        // Verificamos si success es true
        if (!response.isSuccess() || response.getRecords() == null) {
            // Maneja el caso de error o sin datos
            return new VRMMeasurement(siteId, null);
        }

        // El Map data contiene las claves (por ejemplo "143", "144", etc.)
        Map<String, List<List<Double>>> dataMap = response.getRecords().getData();
        if (dataMap == null || dataMap.isEmpty()) {
            // Sin datos
            return new VRMMeasurement(siteId, null);
        }

        String theKey = dataMap.keySet().iterator().next();


        // 2) Obtenemos la lista de [timestamp, valor]
        List<List<Double>> points = dataMap.get(theKey);
        if (points == null || points.isEmpty()) {
            // No hay datos en la lista
            return new VRMMeasurement(siteId, null);
        }

        // 3) Supongamos que queremos el último
        List<Double> lastPair = points.get(points.size() - 1);
        // lastPair[0] => timestamp
        // lastPair[1] => voltage
        Double lastVoltage = null;
        if (lastPair.size() >= 2) {
            lastVoltage = lastPair.get(1);
        }

        // Retornamos un VRMMeasurement con idSite y el voltaje
        return new VRMMeasurement(siteId, lastVoltage);
    }

    @Scheduled(fixedRate = 60000) // Cada 1 minutos
    public void checkVoltagesPeriodically() {
        try {
            List<Settings> optSettings = settingsRepository.findAll();
            Settings settings = optSettings.get(0);
            String tokenVRM = settings.getTokenVRM();
            System.out.println(tokenVRM);
            int idUser = 543462; //ID del usuario

            // 1) Obtener todos los idSites del usuario
            List<Integer> idSites = obtenerIdSites(idUser, tokenVRM);

            // 2) Calcular timestamps start y end (en UNIX seg).
            ZoneId peruZone = ZoneId.of("America/Lima");

            long now = Instant.now().atZone(peruZone).toEpochSecond(); // Timestamp en segundos con zona de Perú
            long fourMinutesAgo = now - 240; // 4 minutos en segundos

            for (Integer siteId : idSites) {
                // 3) Llamar a la API de VRM para extraer voltaje
                VRMGraphResponse graphResp = obtenerGraphData(siteId, fourMinutesAgo, now, tokenVRM);

                // 4) Parsear el voltaje
                VRMMeasurement measurement = parseLastVoltage(graphResp, siteId);

                // 5) Enviar alerta con la condición de (< 46.0)
                Double voltage = measurement.getVoltage();
                if (voltage != null && voltage < 46.0) {
                    String nombreMicrored = obtenerNombreSite(idUser, siteId, tokenVRM);
                    //Consulta a DB para extraer la micronetwork mediante siteVRM
                    Optional optMicronetwork = microNetworkRepository.findBySiteVRM(siteId);
                    MicroNetwork microNetworkchosen = (MicroNetwork) optMicronetwork.get();
                    //Extraer de DB los correosReceptoresArray que tienen el id de la micronetworkchosen
                    System.out.println(microNetworkchosen.getId());
                    List<User> listaUsuariosChosen = usersRepository.findByMicronetworkList(microNetworkchosen.getId());
                    System.out.println(listaUsuariosChosen.get(0).getEmail());
                    //Hacer un for:each en el arreglo
                    for (User userChosen: listaUsuariosChosen){
                        String correoReceptor = userChosen.getEmail();
                        // Enviamos correo
                        if(userChosen.isAlertsEmail()){
                            emailService.sendAlertEmailBateriaBaja(
                                    correoReceptor,
                                    nombreMicrored,
                                    voltage
                            );
                        }
                        else {
                            System.out.println("El usuario " + userChosen.getName() + "no tiene habilitado el envío en correos");
                        }

                    }
                    //Obtener la fecha/hora actual en esa zona
                    LocalDateTime localNow = LocalDateTime.now(peruZone);
                    // Convertir LocalDateTime -> Instant -> Date
                    Instant instant = localNow.atZone(peruZone).toInstant();
                    Date peruDate = Date.from(instant);
                    // Creamos la notificación y la guardamos en DB
                    Notification notification = new Notification();
                    notification.setDescription("Se ha detectado un nivel de voltaje bajo: " + voltage + " V");
                    notification.setTimeCreation(peruDate);
                    notification.setType("BATERÍA BAJA");
                    notification.setIdMicronetwork(microNetworkchosen.getId());
                    notification.setNameMicronetwork(microNetworkchosen.getAlias());
                    notificationRepository.save(notification);

                }
            }

        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al verificar voltajes: " + e.getMessage());
        }
    }

    public String obtenerNombreSite(int idUser, int idSite, String token) throws Exception {
        // Construir la URL del endpoint
        String url = String.format("https://vrmapi.victronenergy.com/v2/users/%d/installations", idUser);

        // Configurar las cabeceras (aquí se utiliza "x-authorization" con el token)
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-authorization", "Token " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        // Realizar la llamada al endpoint
        ResponseEntity<VRMInstallationsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                VRMInstallationsResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            VRMInstallationsResponse body = response.getBody();
            if (body != null && body.isSuccess()) {
                // Recorrer la lista de instalaciones y buscar la que tenga el idSite indicado
                for (VRMInstallationRecord installation : body.getRecords()) {
                    if (installation.getIdSite() == idSite) {
                        return installation.getName();
                    }
                }
                throw new Exception("No se encontró instalación con idSite: " + idSite);
            } else {
                throw new Exception("La respuesta no fue exitosa o está vacía.");
            }
        } else {
            throw new Exception("Error al llamar al endpoint: " + response.getStatusCode());
        }
    }






}
