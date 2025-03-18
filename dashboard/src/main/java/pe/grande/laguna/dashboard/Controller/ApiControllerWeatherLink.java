package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.grande.laguna.dashboard.Dto.WLSensorDTO;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.WeatherLinkService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ApiControllerWeatherLink {

    private final WeatherLinkService weatherLinkService;
    private final SettingsRepository settingsRepository;


    public ApiControllerWeatherLink(WeatherLinkService weatherLinkService, SettingsRepository settingsRepository) {
        this.weatherLinkService = weatherLinkService;
        this.settingsRepository = settingsRepository;
    }





    @GetMapping("/api/weatherlink/current")
    public ResponseEntity<Map<String, Object>> getCurrentWeatherData(@RequestParam("stationId") String stationId) {

        String weatherLinkKey = obtenerKeySeguro();
        String weatherLinkSecret = obtenerSecretSeguro();

        ResponseEntity<Map> responseEntity = weatherLinkService.getCurrentData(stationId, weatherLinkKey, weatherLinkSecret);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map bodyData = responseEntity.getBody();
            // Aquí podrías agregar procesamiento de datos similar al de VRM si fuera necesario.

            System.out.println(ResponseEntity.ok(bodyData));

            return ResponseEntity.ok(bodyData);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }


    @GetMapping("/api/weatherlink/graph-historic")
    public ResponseEntity<Map<String, List<List<Object>>>> getHistoricGraphData(
            @RequestParam("stationId") String stationId,
            @RequestParam("startTimestamp") String startTimestamp,
            @RequestParam("endTimestamp") String endTimestamp) {

        String weatherLinkKey = obtenerKeySeguro();
        String weatherLinkSecret = obtenerSecretSeguro();

        // Llamar a la API para obtener datos históricos
        ResponseEntity<Map> responseHistoricData = weatherLinkService.getHistoricData(
                stationId, weatherLinkKey, weatherLinkSecret, startTimestamp, endTimestamp);

        if (!responseHistoricData.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", new ArrayList<>()));
        }

        Map<String, Object> historicData = responseHistoricData.getBody();

        if (historicData == null || !historicData.containsKey("sensors")) {
            return ResponseEntity.ok(Collections.singletonMap("message", new ArrayList<>()));
        }

        List<Map<String, Object>> sensors = (List<Map<String, Object>>) historicData.get("sensors");

        // Mapa para almacenar datos organizados por tipo de variable
        Map<String, List<List<Object>>> graphData = new HashMap<>();

        for (Map<String, Object> sensor : sensors) {
            Integer sensorType = (Integer) sensor.get("sensor_type");
            List<Map<String, Object>> sensorData = (List<Map<String, Object>>) sensor.get("data");

            for (Map<String, Object> dataPoint : sensorData) {
                Object tsObject = dataPoint.get("ts");
                Long timestamp = null;

                if (tsObject instanceof Integer) {
                    timestamp = ((Integer) tsObject).longValue() * 1000;
                } else if (tsObject instanceof Long) {
                    timestamp = (Long) tsObject * 1000;
                }

                if (timestamp == null) continue;

                for (Map.Entry<String, Object> entry : dataPoint.entrySet()) {
                    String variable = entry.getKey();
                    Object value = entry.getValue();

                    if (!variable.equals("ts") && value instanceof Number) {
                        String key = "sensor_" + sensorType + "." + variable;

                        graphData.putIfAbsent(key, new ArrayList<>());
                        graphData.get(key).add(Arrays.asList(timestamp, value));
                    }
                }
            }
        }

        return ResponseEntity.ok(graphData);
    }



    @GetMapping("/api/weatherlink/sensors-aboard")
    public ResponseEntity<Map<String, Object>> getSensorsAboard(@RequestParam("stationId") String stationId) {

        String weatherLinkKey = obtenerKeySeguro();
        String weatherLinkSecret = obtenerSecretSeguro();

        ResponseEntity<Map> responseCurrentSensors = weatherLinkService.getCurrentData(stationId, weatherLinkKey, weatherLinkSecret);
        ResponseEntity<Map> responseCatalogSensors = weatherLinkService.getSensorCatalog(weatherLinkKey, weatherLinkSecret);

        if (!responseCurrentSensors.getStatusCode().is2xxSuccessful() || !responseCatalogSensors.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Error al obtener datos de WeatherLink"));
        }

        // Extraer datos
        Map<String, Object> currentData = responseCurrentSensors.getBody();
        Map<String, Object> catalogData = responseCatalogSensors.getBody();

        System.out.println(currentData);
        System.out.println(responseCurrentSensors);


        if (currentData == null || catalogData == null || !currentData.containsKey("sensors") || !catalogData.containsKey("sensor_types")) {

            return ResponseEntity.ok(Collections.singletonMap("sensors", Collections.emptyList()));
        }

        List<Map<String, Object>> currentSensors = (List<Map<String, Object>>) currentData.get("sensors");
        List<Map<String, Object>> catalogSensors = (List<Map<String, Object>>) catalogData.get("sensor_types");

        // Crear un mapa de búsqueda para facilitar la correlación
        Map<Integer, Map<String, Object>> sensorCatalogMap = catalogSensors.stream()
                .collect(Collectors.toMap(s -> (Integer) s.get("sensor_type"), s -> s));

        List<WLSensorDTO> enrichedSensors = currentSensors.stream()
                .map(sensor -> fullSensorData(sensor, sensorCatalogMap))
                .collect(Collectors.toList());

        // Retornar como un Map para cumplir con la estructura esperada
        return ResponseEntity.ok(Collections.singletonMap("sensors", enrichedSensors));
    }


    private WLSensorDTO fullSensorData(Map<String, Object> sensorData, Map<Integer, Map<String, Object>> sensorCatalogMap) {

        WLSensorDTO dto = new WLSensorDTO();

        int lsid = (int) sensorData.get("lsid");
        int sensorType = (int) sensorData.get("sensor_type");
        int dataStructureType = (int) sensorData.get("data_structure_type");

        dto.setLsid(lsid);
        dto.setSensorType(sensorType);
        dto.setDataStructureType(dataStructureType);

        // Buscar metadatos en el catálogo
        Map<String, Object> metadata = sensorCatalogMap.get(sensorType);
        if (metadata != null) {
            dto.setManufacturer((String) metadata.get("manufacturer"));
            dto.setProductName((String) metadata.get("product_name"));
            dto.setProductNumber((String) metadata.get("product_number"));
            dto.setCategory((String) metadata.get("category"));

            // Buscar la descripción de la estructura de datos
            List<Map<String, Object>> dataStructures = (List<Map<String, Object>>) metadata.get("data_structures");
            if (dataStructures != null) {
                for (Map<String, Object> structure : dataStructures) {
                    if (((String) structure.get("data_structure_type")).equals(String.valueOf(dataStructureType))) {
                        dto.setDataStructureDescription((String) structure.get("description"));
                        break;
                    }
                }
            }
        }

        // Extraer los datos más recientes del sensor
        List<Map<String, Object>> sensorReadings = (List<Map<String, Object>>) sensorData.get("data");
        if (sensorReadings != null && !sensorReadings.isEmpty()) {
            dto.setLatestData(sensorReadings.get(sensorReadings.size() - 1)); // Último registro
        }

        return dto;
    }



    /**
     * No exponer los tokens de las APIs
     */

    private String obtenerKeySeguro() {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getKeyWeatherLink();
    }

    private String obtenerSecretSeguro() {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getSecretWeatherLink();
    }


}
