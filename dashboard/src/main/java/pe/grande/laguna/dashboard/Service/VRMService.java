package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class VRMService {

    private static final String VRM_BASE_URL = "https://vrmapi.victronenergy.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();


    public ResponseEntity<Map> getInstallations(String token) {
        String url = VRM_BASE_URL + "/installations";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }

    public ResponseEntity<Map> getInstallationData(String token, String siteId) {
        String url = VRM_BASE_URL + "/installations/" + siteId + "/stats";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }
}
