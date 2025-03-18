package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class WeatherLinkService {

    private static final String WL_BASE_URL = "https://api.weatherlink.com/v2";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<Map> getHistoricData(String stationId, String apiKey, String apiSecret, String startTimestamp, String endTimestamp) {
        // Construimos la URL base
        String baseUrl = WL_BASE_URL + "/historic/" + stationId;

        // Añadimos los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("api-key", apiKey)
                .queryParam("start-timestamp", startTimestamp)
                .queryParam("end-timestamp", endTimestamp)
                .toUriString();

        // Mostramos la URL final para verificarla
        System.out.println("✅ WeatherLink URL final: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-secret", apiSecret);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET al endpoint de WeatherLink
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }



    public ResponseEntity<Map> getCurrentData(String stationId, String apiKey, String apiSecret) {
        // Construimos la URL base
        String baseUrl = WL_BASE_URL + "/current/" + stationId;

        // Añadimos los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("api-key", apiKey)
                .toUriString();

        // Mostramos la URL final para verificarla
        System.out.println("✅ WeatherLink URL final: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-secret", apiSecret);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET al endpoint de WeatherLink
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }



    public ResponseEntity<Map> getSensorCatalog(String apiKey, String apiSecret) {
        // Construimos la URL base
        String baseUrl = WL_BASE_URL + "/sensor-catalog";

        // Añadimos los parámetros de forma segura
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("api-key", apiKey)
                .toUriString();

        // Mostramos la URL final para verificarla
        System.out.println("✅ WeatherLink URL final: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-api-secret", apiSecret);

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Realizamos la llamada GET al endpoint de WeatherLink
        return restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
    }




}
