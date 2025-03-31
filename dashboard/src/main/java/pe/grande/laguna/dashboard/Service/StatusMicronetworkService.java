package pe.grande.laguna.dashboard.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;

import java.util.Collections;
import java.util.List;

@Service
public class StatusMicronetworkService {

    private final MicroNetworkRepository microNetworkRepository;
    private final SettingsRepository settingsRepository;

    public StatusMicronetworkService(MicroNetworkRepository microNetworkRepository, SettingsRepository settingsRepository) {
        this.microNetworkRepository = microNetworkRepository;
        this.settingsRepository = settingsRepository;
    }


    @Scheduled(fixedRate = 60000*6) // Cada 6 minutos
    public void StatusMicronetwork(){
        System.out.println("Comprobando...");

        //Extraer el idSite de las 3 plataformas de cada micronetwork
        List<MicroNetwork> listaMicronetworks = microNetworkRepository.findAll();
        Integer idUser = 543462; //ID USUARIO

        List<Settings> listaSettings = settingsRepository.findAll();
        Settings settings = listaSettings.get(0);

        // Settings de WeatherLink
        String apiKeyWeatherLink = settings.getKeyWeatherLink();
        String xApiSecretWeatherLink = settings.getSecretWeatherLink();

        // Settings de VRM
        String tokenVRM = settings.getTokenVRM();

        // Settings de SparkMeter
        String keySparkmeter = settings.getKeySparkMeter();
        String secretSparkmeter = settings.getSecretSparkMeter();

        for (MicroNetwork microNetworkIterador: listaMicronetworks){
            String siteSparkmeter = microNetworkIterador.getSiteSparkMeter();
            Integer siteVRM = microNetworkIterador.getSiteVRM();
            Integer siteWeatherLink = microNetworkIterador.getSiteWeatherLink();

            //Hacer consultas a cada plataforma con los sites
            String urlVRM = "https://vrmapi.victronenergy.com/v2/installations/" + siteVRM + "/stats";
            // Crear metodo que devuelva True o False en respuesta a la URL que se va a usar para VRM
            boolean estadoVRM = checkVRMConnection(urlVRM, tokenVRM);

            String urlWeatherLink = "https://api.weatherlink.com/v2/stations/" + siteWeatherLink + "?api-key=" + apiKeyWeatherLink;
            // Crear metodo que devuelva True o False en respuesta a la URL que se va a usar para WeatherLink
            boolean estadoWeatherLink = checkWeatherLinkConnection(urlWeatherLink, xApiSecretWeatherLink);


            String urlSparkMeter = "https://www.sparkmeter.cloud/api/v1/service_areas";
            // Crear metodo que devuelva True o False en respuesta a la URL que se va a usar para SparkMeter
            boolean estadoSparkmeter = checkSparkMeterConnection(urlSparkMeter, keySparkmeter, secretSparkmeter, siteSparkmeter);

            //si alguna de las consultas no es buena entonces cambia el estado de ACTIVO a INACTIVO
            if(estadoVRM && estadoWeatherLink && estadoSparkmeter){
                System.out.println("Todo está bien para la instalación: " + microNetworkIterador.getAlias());
            }else {
                System.out.println("STATUS: INACTIVO para " + microNetworkIterador.getAlias());
                microNetworkIterador.setStatus("INACTIVO");
                microNetworkRepository.save(microNetworkIterador);
            }

        }
    }
    public boolean checkSparkMeterConnection(String urlSparkMeter, String keySparkmeter, String secretSparkmeter, String siteSparkmeter) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-KEY", keySparkmeter);
            headers.set("X-API-SECRET", secretSparkmeter);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Crear entidad con headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Hacer la solicitud GET
            ResponseEntity<String> response = restTemplate.exchange(
                    urlSparkMeter,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                return false;
            }

            // Parsear JSON y buscar el ID
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode data = root.path("data");
            if (data.isArray()) {
                for (JsonNode item : data) {
                    if (item.has("id") && item.get("id").asText().equals(siteSparkmeter)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println("Error al consultar SparkMeter: " + e.getMessage());
            return false;
        }
    }



    public boolean checkVRMConnection(String urlVRM, String tokenVRM) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-authorization", "Token " + tokenVRM);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Crear la entidad con headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Hacer la solicitud GET
            ResponseEntity<String> response = restTemplate.exchange(
                    urlVRM,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Verificar si el status code es 2xx

            if(response.getStatusCode().is2xxSuccessful()){
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al consultar VRM: " + e.getMessage());
            return false;
        }
    }

    public boolean checkWeatherLinkConnection(String urlWeatherLink, String xApiSecretWeatherLink) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Crear headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-secret", xApiSecretWeatherLink);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Crear la entidad con headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Hacer la solicitud GET
            ResponseEntity<String> response = restTemplate.exchange(
                    urlWeatherLink,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Verificar si el status code es 2xx
            if(response.getStatusCode().is2xxSuccessful()){
                return true;
            }
            else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Error al consultar WeatherLink: " + e.getMessage());
            return false;
        }
    }







}
