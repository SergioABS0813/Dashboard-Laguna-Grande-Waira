package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.grande.laguna.dashboard.Dto.VRMDeviceDTO;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.VRMService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ApiControllerVRM {

    private final VRMService vrmService;
    private final SettingsRepository settingsRepository;


    List<String> ACLoadCodes = Arrays.asList("a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9");
    List<String> MPPTCodes = Arrays.asList("PVP", "OP2", "OP3", "OP4", "OP5", "OP6", "OP7", "OP8", "OP9");
    List<String> BatteryVoltageCodes = List.of("CV");

    List<String> allowedCodes = Arrays.asList("a1", "a2", "a3", "CV", "PVP");


    public ApiControllerVRM(VRMService vrmService, SettingsRepository settingsRepository) {
        this.vrmService = vrmService;
        this.settingsRepository = settingsRepository;
    }

    @GetMapping("/api/graph-data")
    public ResponseEntity<Map<String, Object>> getMainGraphData(@RequestParam("siteId") String siteId,
                                                            @RequestParam(value = "interval", required = false) String interval,
                                                            @RequestParam("start") String start,
                                                            @RequestParam("end") String end) {

        String token = obtenerTokenSeguro();

        // Invoca el servicio para obtener los datos de la instalación
        ResponseEntity<Map> responseEntity = vrmService.getMainData(token, siteId, interval, start, end);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map installationData = responseEntity.getBody();

            // Procesa los datos obtenidos para extraer la información para las gráficas
            Map<String, Object> graphData = processDataMainGraph(installationData);

            return ResponseEntity.ok(graphData);
        } else {
            // En caso de error, se retorna el código de estado correspondiente
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }


    @GetMapping("/api/devices-and-attributes")
    public ResponseEntity<Map<String, Object>> getDevicesAndAttributes(@RequestParam("siteId") String siteId) {

        String token = obtenerTokenSeguro();

        // Invoca el servicio para obtener componentes
        ResponseEntity<Map> responseEntityComponents = vrmService.getComponentsData(token, siteId);
        // Invoca el servicio para obtener los atributos
        ResponseEntity<Map> responseEntityAttributes = vrmService.getDataAttributesData(token, siteId);

        if (responseEntityComponents.getStatusCode().is2xxSuccessful() && responseEntityAttributes.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> componentsData = responseEntityComponents.getBody();
            Map<String, Object> attributesData = responseEntityAttributes.getBody();

            List<VRMDeviceDTO> devices = new ArrayList<>();
            List<Map<String, Object>> filteredAttributes = new ArrayList<>();

            // Extraer dispositivos de componentsData
            if (componentsData != null && componentsData.containsKey("records")) {
                Map<String, Object> records = (Map<String, Object>) componentsData.get("records");

                if (records.containsKey("devices")) {
                    List<Map<String, Object>> deviceList = (List<Map<String, Object>>) records.get("devices");
                    devices = mapToDevices(deviceList);
                }
            }

            if (attributesData != null && attributesData.containsKey("result")) {
                Map<String, Object> result = (Map<String, Object>) attributesData.get("result");

                if (result.containsKey("attributes")) {
                    List<Map<String, Object>> attributesList = (List<Map<String, Object>>) result.get("attributes");

                    for (VRMDeviceDTO device : devices) {
                        for (Map<String, Object> attribute : attributesList) {
                            Integer instance = device.getInstance();
                            String code = (String) attribute.get("code");

                            if (allowedCodes.contains(code)) {
                                attribute.put("instance", instance);
                                attribute.put("code", code);
                                filteredAttributes.add(attribute);
                            }

                        }
                    }
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("devices", devices);
            response.put("filteredAttributes", filteredAttributes);

            System.out.println("Atributos filtrados: " + filteredAttributes.size());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(responseEntityComponents.getStatusCode()).body(null);
        }
    }


    @GetMapping("/api/graph-widgets")
    public ResponseEntity<Map<String, Object>> getWidgetGraphData(@RequestParam("siteId") String siteId,
                                                                  @RequestParam("attributeIds") String attributeId,
                                                                  @RequestParam("instance") String instance,
                                                                  @RequestParam("start") String start,
                                                                  @RequestParam("end") String end) {

        String token = obtenerTokenSeguro();

        // Invoca el servicio para obtener los datos de la instalación
        ResponseEntity<Map> responseEntity = vrmService.getDataWidget(token, siteId, attributeId, instance, start, end);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map bodyData = responseEntity.getBody();

            // Procesa los datos obtenidos para extraer la información para las gráficas
            Map<String, Object> processedData = processDataWidgets(bodyData);

            return ResponseEntity.ok(processedData);
        } else {
            // En caso de error, se retorna el código de estado correspondiente
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
    }










    private Map<String, Object> processDataMainGraph(Map installationData) {
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

            // Procesar los totales (totals)
            if (installationData.containsKey("totals")) {
                Object totalsObj = installationData.get("totals");
                if (totalsObj instanceof Map) {
                    Map totals = (Map) totalsObj;
                    Map<String, Object> processedTotals = new HashMap<>();
                    for (Object key : totals.keySet()) {
                        Object value = totals.get(key);
                        // Si es un número, redondearlo a 2 decimales
                        if (value instanceof Number) {
                            double rounded = roundToTwoDecimals(((Number) value).doubleValue());
                            processedTotals.put(key.toString(), rounded);
                        } else {
                            processedTotals.put(key.toString(), value);
                        }
                    }
                    graphData.put("totals", processedTotals);
                }
            }

        }
        return graphData;
    }


    private Map<String, Object> processDataWidgets(Map<String, Object> bodyData) {
        Map<String, Object> processedData = new HashMap<>();

        if (bodyData != null && bodyData.containsKey("records")) {
            Object recordsObj = bodyData.get("records");

            if (recordsObj instanceof Map) {
                Map<String, Object> records = (Map<String, Object>) recordsObj;

                if (records.containsKey("data")) {
                    Object dataObj = records.get("data");

                    if (dataObj instanceof Map) {
                        Map<String, List<List<Number>>> dataMap = (Map<String, List<List<Number>>>) dataObj;
                        Map<String, List<List<Number>>> formattedData = new HashMap<>();

                        for (Map.Entry<String, List<List<Number>>> entry : dataMap.entrySet()) {
                            String attributeId = entry.getKey();
                            List<List<Number>> rawData = entry.getValue();

                            // 🔹 Evita agregar datos vacíos
                            if (rawData == null || rawData.isEmpty()) {
                                System.out.println("⚠️ Datos vacíos para atributo: " + attributeId + ", no se incluirá.");
                                continue;
                            }

                            List<List<Number>> formattedList = new ArrayList<>();

                            for (List<Number> row : rawData) {
                                if (row.size() >= 2) {
                                    Number timestamp = row.get(0);
                                    Number value = row.get(1);

                                    if (value != null) {
                                        double roundedValue = roundToTwoDecimals(value.doubleValue());
                                        formattedList.add(Arrays.asList(timestamp.longValue(), roundedValue));
                                    }
                                }
                            }

                            // 🔹 Solo agregar si hay datos válidos
                            if (!formattedList.isEmpty()) {
                                formattedData.put(attributeId, formattedList);
                            }
                        }

                        processedData.put("data", formattedData);
                    }
                }

                if (records.containsKey("meta")) {
                    processedData.put("meta", records.get("meta"));
                }

                if (records.containsKey("instance")) {
                    processedData.put("instance", records.get("instance"));
                }
            }
        }

        return processedData;
    }




    /**
     * No exponer los tokens de las APIs
     */

    private String obtenerTokenSeguro(){

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        return settings.getTokenVRM();
    }


    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }


    public static List<VRMDeviceDTO> mapToDevices(List<Map<String, Object>> deviceList) {
            return deviceList.stream().map(deviceMap -> {
                VRMDeviceDTO device = new VRMDeviceDTO();
                device.setMachineSerialNumber((String) deviceMap.get("machineSerialNumber"));
                device.setName((String) deviceMap.get("name"));
                device.setProductName((String) deviceMap.get("productName"));
                device.setFirmwareVersion((String) deviceMap.get("firmwareVersion"));
                device.setIdDeviceType((Integer) deviceMap.get("idDeviceType"));
                device.setInstance((Integer) deviceMap.get("instance"));
                device.setIdSite((Integer) deviceMap.get("idSite"));
                return device;
            }).collect(Collectors.toList());
    }

}

