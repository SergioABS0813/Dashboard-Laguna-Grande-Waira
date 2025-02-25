package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.VRMService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiControllerVRM {

    private final VRMService vrmService;
    private final SettingsRepository settingsRepository;

    public ApiControllerVRM(VRMService vrmService, SettingsRepository settingsRepository) {
        this.vrmService = vrmService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/api/graph-data")
    public ResponseEntity<Map<String, Object>> getGraphData(@RequestParam("siteId") String siteId) {
        // Obtención segura del token (puede ser de variables de entorno, un servicio, etc.)
        String token = obtenerTokenSeguro();

        // Invoca el servicio para obtener los datos de la instalación
        ResponseEntity<Map> responseEntity = vrmService.getInstallationData(token, siteId);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map installationData = responseEntity.getBody();

            // Procesa los datos obtenidos para extraer la información para las gráficas
            Map<String, Object> graphData = procesarDatosParaGraficas(installationData);

            return ResponseEntity.ok(graphData);
        } else {
            // En caso de error, se retorna el código de estado correspondiente
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }

    /**
     * Simula la obtención segura de un token. En producción, este método debe obtener
     * el token desde una fuente segura (por ejemplo, variables de entorno o un servicio de autenticación).
     */
    private String obtenerTokenSeguro(){

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getTokenVRM();
    }

    /**
     * Procesa la respuesta de la API para extraer la información necesaria para las gráficas.
     * Se asume que la respuesta contiene, por ejemplo, "timestamps" y "values" que se usarán
     * como etiquetas y datos respectivamente.
     */

    private Map<String, Object> procesarDatosParaGraficas(Map installationData) {
        Map<String, Object> graphData = new HashMap<>();
        if (installationData != null && installationData.containsKey("records")) {
            Map records = (Map) installationData.get("records");
            // Extraer la serie "Pdc" si existe
            if (records.containsKey("Pdc")) {
                graphData.put("Pdc", records.get("Pdc"));
            }
            // Extraer la serie "total_solar_yield" si existe
            if (records.containsKey("total_solar_yield")) {
                graphData.put("total_solar_yield", records.get("total_solar_yield"));
            }
            // Puedes agregar aquí otras series, por ejemplo "total_consumption", "bv", etc.
        }
        return graphData;
    }

}

