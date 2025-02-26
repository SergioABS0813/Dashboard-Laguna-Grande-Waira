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


    public ResponseEntity<Map> getInstallations(String tokenVRM) {
        String url = VRM_BASE_URL + "/installations";

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }

    public ResponseEntity<Map> getInstallationData(String tokenVRM, String siteId, String interval, String start, String end) {
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

}
