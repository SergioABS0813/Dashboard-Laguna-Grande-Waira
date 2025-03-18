package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.VRMGraphResponse;
import pe.grande.laguna.dashboard.Dto.ResponsesJSON.VRMInstallationsResponse;
import pe.grande.laguna.dashboard.Dto.VRMMeasurement;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VRMService {

    private static final String VRM_BASE_URL = "https://vrmapi.victronenergy.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();
    private final EmailService emailService;
    private final SettingsRepository settingsRepository;

    public VRMService(EmailService emailService, SettingsRepository settingsRepository) {
        this.emailService = emailService;
        this.settingsRepository = settingsRepository;
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

            String token = "6e23e410c0b8981b5362f844004770f7c71480949bec44f320a3c6155d45846e"; // Recuperar desde BD o configuración
            int idUser = 543462;

            // 1) Obtener todos los idSites del usuario
            List<Integer> idSites = obtenerIdSites(idUser, token);

            // 2) Calcular timestamps start y end (en UNIX seg).
            // Falta setear para que sea hora peruana
            long now = System.currentTimeMillis() / 1000; // Actual en seg
            long fourMinutesAgo = now - 240;             // 4 min en seg

            for (Integer siteId : idSites) {
                // 3) Llamar a la API de VRM para extraer voltaje (por ej. 'bv')
                VRMGraphResponse graphResp = obtenerGraphData(siteId, fourMinutesAgo, now, token);

                // 4) Parsear el voltaje (o la medición) que te interesa
                VRMMeasurement measurement = parseLastVoltage(graphResp, siteId);

                // 5) Si el voltaje excede cierto umbral, enviar alerta
                Double voltage = measurement.getVoltage();
                if (voltage != null && voltage > 50.0) {
                    // Supongamos que tienes un método para obtener el nombre
                    // de la instalación (p. ej. "Laguna Grande") en base a siteId:
                    String nombreMicrored = "Laguna Grande"; //Sería hacer una consulta a la API VRM
                    //String nombreMicrored = obtenerNombreMicrored(siteId);

                    // Igualmente, si quieres enviar el correo al usuario final,
                    // obtienes su correo de tu BD o de la config:
                    String correoReceptor = "a20213170@pucp.edu.pe";

                    // Llamas a tu EmailService
                    emailService.sendAlertEmailSobrecarga(
                            correoReceptor,
                            nombreMicrored,
                            voltage
                    );
                }

                // FALTA CREAR LAS NOTIFICACIONES Y COLOCARLAS A BD
            }

        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al verificar voltajes: " + e.getMessage());
        }
    }






}
