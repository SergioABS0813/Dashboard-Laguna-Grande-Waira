package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.VRMService;

import java.util.*;

@RestController
public class ApiControllerVRM {

    private final VRMService vrmService;
    private final SettingsRepository settingsRepository;

    public ApiControllerVRM(VRMService vrmService, SettingsRepository settingsRepository) {
        this.vrmService = vrmService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/api/graph-data")
    public ResponseEntity<Map<String, Object>> getGraphData(@RequestParam("siteId") String siteId,
                                                            @RequestParam(value = "interval", required = false) String interval,
                                                            @RequestParam("start") String start,
                                                            @RequestParam("end") String end) {

        // Obtención segura del token (puede ser de variables de entorno, un servicio, etc.)
        String token = obtenerTokenSeguro();

        // Invoca el servicio para obtener los datos de la instalación
        ResponseEntity<Map> responseEntity = vrmService.getInstallationData(token, siteId, interval, start, end);

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

            // total_solar_yield
            if (records.containsKey("total_solar_yield")) {
                Object solarObj = records.get("total_solar_yield");
                if (solarObj instanceof Iterable) {
                    List<List<Number>> newSolarList = new ArrayList<>();
                    for (Object item : (Iterable) solarObj) {
                        if (item instanceof List) {
                            List row = (List) item;
                            if (row.size() >= 2) {
                                // row(0) = timestamp (no se redondea)
                                Number ts = (Number) row.get(0);
                                Number val = (Number) row.get(1);
                                double roundedVal = roundToTwoDecimals(val.doubleValue());
                                // Construimos el nuevo par [timestamp, valor redondeado]
                                newSolarList.add(Arrays.asList(ts.longValue(), roundedVal));
                            }
                        }
                    }
                    graphData.put("total_solar_yield", newSolarList);
                }
            }

            // total_consumption
            if (records.containsKey("total_consumption")) {
                Object consObj = records.get("total_consumption");
                if (consObj instanceof Iterable) {
                    List<List<Number>> newConsList = new ArrayList<>();
                    for (Object item : (Iterable) consObj) {
                        if (item instanceof List) {
                            List row = (List) item;
                            if (row.size() >= 2) {
                                // row(0) = timestamp (no se redondea)
                                Number ts = (Number) row.get(0);
                                Number val = (Number) row.get(1);
                                double roundedVal = roundToTwoDecimals(val.doubleValue());
                                newConsList.add(Arrays.asList(ts.longValue(), roundedVal));
                            }
                        }
                    }
                    graphData.put("total_consumption", newConsList);
                }
            }

            // bv: separamos en bv_avg ( [ts, avg] ) y bv_range ( [ts, min, max] )
            if (records.containsKey("bv")) {
                Object bvObj = records.get("bv");
                if (bvObj instanceof Iterable) {
                    List<List<Number>> bv_avg = new ArrayList<>();
                    List<List<Number>> bv_range = new ArrayList<>();

                    for (Object item : (Iterable) bvObj) {
                        if (item instanceof List) {
                            List row = (List) item;
                            if (row.size() >= 4) {
                                // row(0)=timestamp (no se redondea)
                                // row(1)=average, row(2)=min, row(3)=max (se redondean)
                                Number ts = (Number) row.get(0);
                                Number avgVal = (Number) row.get(1);
                                Number minVal = (Number) row.get(2);
                                Number maxVal = (Number) row.get(3);

                                double roundedAvg = roundToTwoDecimals(avgVal.doubleValue());
                                double roundedMin = roundToTwoDecimals(minVal.doubleValue());
                                double roundedMax = roundToTwoDecimals(maxVal.doubleValue());

                                // bv_avg -> [ts, average]
                                bv_avg.add(Arrays.asList(ts.longValue(), roundedAvg));
                                // bv_range -> [ts, min, max]
                                bv_range.add(Arrays.asList(ts.longValue(), roundedMin, roundedMax));
                            }
                        }
                    }
                    graphData.put("bv_avg", bv_avg);
                    graphData.put("bv_range", bv_range);
                }
            }
        }
        return graphData;
    }

    /**
     * Redondea (round half up) a dos decimales.
     * Si deseas siempre redondear hacia arriba, cambia Math.round() por Math.ceil().
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


}

