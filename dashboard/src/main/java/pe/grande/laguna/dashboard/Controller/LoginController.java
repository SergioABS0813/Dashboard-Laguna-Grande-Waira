package pe.grande.laguna.dashboard.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping({"/", "/login", "/login/"})
    public String login() {
        return "index";
    }

}
