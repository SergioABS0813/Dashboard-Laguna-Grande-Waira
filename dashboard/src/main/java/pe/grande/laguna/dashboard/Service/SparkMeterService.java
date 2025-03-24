package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pe.grande.laguna.dashboard.Entity.Settings;

import java.util.Map;

@Service
public class SparkMeterService {

    private static final String SPARKMETER_BASE_URL = "https://www.sparkmeter.cloud/api/v1";
    private final RestTemplate restTemplate = new RestTemplate();

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

}
