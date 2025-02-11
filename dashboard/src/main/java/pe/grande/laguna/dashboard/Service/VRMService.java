package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class VRMService {

    private static final String VRM_AUTH_URL = "https://vrmapi.victronenergy.com/v2/auth/login";
    private static final String VRM_BASE_URL = "https://vrmapi.victronenergy.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Map<String, Object>> login(Map<String, Object> credentials) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(credentials, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(VRM_AUTH_URL, request, Map.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

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
