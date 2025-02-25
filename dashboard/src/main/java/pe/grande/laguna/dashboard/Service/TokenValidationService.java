package pe.grande.laguna.dashboard.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenValidationService {

    private final RestTemplate restTemplate;

    public TokenValidationService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean validateWeatherLink(String apiKey, String apiSecret) {
        // Endpoint
        String url = "https://api.weatherlink.com/v2/nodes?api-key=" + apiKey;

        try {
            // Construir los headers
            HttpHeaders headers = new HttpHeaders();
            // El doc indica: "x-api-secret" es el nombre del header
            headers.add("x-api-secret", apiSecret);

            // Crear la entidad que representa la solicitud (sin body, solo headers)
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // Usar exchange con método GET
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            // Verificar el código de estado
            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            // Si hay error (conexión, 4xx, 5xx, etc.), consideramos token inválido
            return false;
        }
    }


    public boolean validateSparkMeter(String key, String secret) {
        // Endpoint real para consultar service_areas
        String url = "https://sparkmeter.cloud/api/v1/service_areas";

        try {
            // 1. Construir los headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-API-KEY", key);
            headers.add("X-API-SECRET", secret);
            // Opcional: forzar la cabecera Accept a application/json
            //headers.add("Accept", "application/json");

            // Crear la entidad que representa la solicitud (sin body, solo headers)
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // Usar exchange con método GET
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            // Verificar el código de estado
            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            // Si hay error (conexión, 4xx, 5xx, etc.), consideramos token inválido
            return false;
        }
    }


    public boolean validateVRM(String tokenVRM) {
        //En base a este endpoint se corroborará si el Token permanente es o no correcto
        String url = "https://vrmapi.victronenergy.com/v2/users/me";

        try {
            // Construir los headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("x-authorization", "Token " + tokenVRM);

            // Crear la entidad que representa la solicitud (sin body, solo headers)
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            // Usar exchange en lugar de getForEntity
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

}
