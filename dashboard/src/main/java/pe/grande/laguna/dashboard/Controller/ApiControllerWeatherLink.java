package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Service.WeatherLinkService;

import java.util.Map;

@RestController
public class ApiControllerWeatherLink {

    private final WeatherLinkService weatherLinkService;
    private final SettingsRepository settingsRepository;


    public ApiControllerWeatherLink(WeatherLinkService weatherLinkService, SettingsRepository settingsRepository) {
        this.weatherLinkService = weatherLinkService;
        this.settingsRepository = settingsRepository;
    }


    @GetMapping("/api/weatherlink/historic")
    public ResponseEntity<Map<String, Object>> getHistoricWeatherData(@RequestParam("stationId") String stationId,
                                                                      @RequestParam("start-timestamp") String startTimestamp,
                                                                      @RequestParam("end-timestamp") String endTimestamp) {

        String weatherLinkKey = obtenerKeySeguro();
        String weatherLinkSecret = obtenerSecretSeguro();

        ResponseEntity<Map> responseEntity = weatherLinkService.getHistoricData(stationId, weatherLinkKey, weatherLinkSecret, startTimestamp, endTimestamp);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map bodyData = responseEntity.getBody();
            // Aquí podrías agregar procesamiento de datos similar al de VRM si fuera necesario.
            return ResponseEntity.ok(bodyData);
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
        }
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
