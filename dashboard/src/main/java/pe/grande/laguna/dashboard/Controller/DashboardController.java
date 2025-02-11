package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pe.grande.laguna.dashboard.Service.VRMService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    private final VRMService VRMService;

    public DashboardController(VRMService VRMService) {
        this.VRMService = VRMService;
    }


    @GetMapping("/dashboard")
    public String dashboard() {

        Map<String, Object> credentials = new HashMap<>();

        // Las credenciales tendrán que sacarse de DB, el usuario va a configurar los accesos a cada plataforma (consultas a DB)

        credentials.put("username", "");
        credentials.put("password", "");

        ResponseEntity<Map<String, Object>> response = VRMService.login(credentials);

        // Agregar en el if la condición de que el usuario logueado no tenga token generado para VRM (consulta a DB)

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String token = "Bearer " + response.getBody().get("token");
            System.out.println( token );

            // Guardar el token generado en DB, el token se podrá re-generar

        }

        return "dashboard";
    }

}
