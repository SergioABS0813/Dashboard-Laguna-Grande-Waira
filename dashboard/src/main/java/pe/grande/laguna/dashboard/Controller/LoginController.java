package pe.grande.laguna.dashboard.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /*
    * FALTA LA LÓGICA DEL LOGIN (Definir DB, quizás MongoDB)
    * */


    @GetMapping({"/", "/login", "/login/"})
    public String login() {

        return "login";
    }

}
