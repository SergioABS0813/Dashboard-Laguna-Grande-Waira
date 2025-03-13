package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class VRMService {

    private static final String VRM_BASE_URL = "https://vrmapi.victronenergy.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();


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


}
