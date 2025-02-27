package pe.grande.laguna.dashboard.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.grande.laguna.dashboard.Dto.InstalacionDTO;
import pe.grande.laguna.dashboard.Dto.InstalacionSparkmeterDTO;

import java.util.ArrayList;

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


    //SERÁN REEMPLAZADOS POR ESTOS:

    public ArrayList<InstalacionDTO> obtenerListaInstalaciones(String tokenVRM) throws JsonProcessingException {
        ArrayList<InstalacionDTO> listaInstalaciones = new ArrayList<>();

        // URL de la API
        String url = "https://vrmapi.victronenergy.com/v2/users/543462/installations";

        // Construir los headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-authorization", "Token " + tokenVRM);

        // Crear la entidad que representa la solicitud (sin body, solo headers)
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Usar exchange con método GET para incluir los headers
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Parsear la respuesta JSON
        String jsonBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonBody);

        // Extraer el nodo "records" y recorrer
        JsonNode records = root.get("records");
        for (JsonNode record : records) {
            int idSite = record.get("idSite").asInt();
            String name = record.get("name").asText();

            InstalacionDTO dto = new InstalacionDTO(name, idSite);
            listaInstalaciones.add(dto);
        }

        return listaInstalaciones;
    }

    public ArrayList<InstalacionDTO> obtenerListaInstalacionesWeatherLink(String apiKey, String apiSecret) throws JsonProcessingException {
        ArrayList<InstalacionDTO> listaInstalacionesWeatherLink = new ArrayList<>();

        // URL de la API
        String url = "https://api.weatherlink.com/v2/stations?api-key=" + apiKey;

        // Construir los headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-secret", apiSecret);

        // Crear la entidad que representa la solicitud (sin body, solo headers)
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Usar exchange con método GET para incluir los headers
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Parsear la respuesta JSON
        String jsonBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonBody);

        // Extraer el nodo "records" y recorrer
        JsonNode records = root.get("stations");
        for (JsonNode record : records) {
            int idSite = record.get("station_id").asInt();
            String name = record.get("station_name").asText();

            InstalacionDTO dto = new InstalacionDTO(name, idSite);
            listaInstalacionesWeatherLink.add(dto);
        }

        return listaInstalacionesWeatherLink;
    }

    public ArrayList<InstalacionSparkmeterDTO> obtenerListaInstalacionesSparkmeter(String key, String secret) throws JsonProcessingException {
        ArrayList<InstalacionSparkmeterDTO> listaInstalacionesSparkmeter = new ArrayList<>();

        // URL de la API
        String url = "https://www.sparkmeter.cloud/api/v1/service_areas";

        // Construir los headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-API-KEY", key);
        headers.add("X-API-SECRET", secret);

        // Crear la entidad que representa la solicitud (sin body, solo headers)
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Usar exchange con método GET para incluir los headers
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        // Parsear la respuesta JSON
        String jsonBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonBody);

        // Extraer el nodo "records" y recorrer
        JsonNode records = root.get("data");
        for (JsonNode record : records) {
            String idSite = record.get("id").asText();
            String name = record.get("name").asText();

            InstalacionSparkmeterDTO dto = new InstalacionSparkmeterDTO(name, idSite);
            listaInstalacionesSparkmeter.add(dto);
        }

        return listaInstalacionesSparkmeter;
    }









}
