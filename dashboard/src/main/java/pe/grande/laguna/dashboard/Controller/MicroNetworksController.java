package pe.grande.laguna.dashboard.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MicroNetworksController {

    @GetMapping("/micronetworks")
    public String table() {

        return "table_micronetworks";
    }
}
