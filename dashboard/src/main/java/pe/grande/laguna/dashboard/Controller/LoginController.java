package pe.grande.laguna.dashboard.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.Optional;

@Controller
public class LoginController {

    /*
    * FALTA LA LÓGICA DEL LOGIN (Definir DB, quizás MongoDB)
    * */

    final UsersRepository usersRepository;

    public LoginController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping({"/", "/login", "/login/"})
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si el usuario ya está autenticado, lo redirigimos al dashboard
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            System.out.println("EL USUARIO YA ESTÁ AUTENTICADO");
            return "redirect:/micronetworks";
        }

        if (error != null) {

            System.out.println(auth.getName());

            System.out.println("CREDENCIALES INCORRECTAS");
            model.addAttribute("errorMessage", "Credenciales incorrectas. Intenta nuevamente.");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Sesión cerrada exitosamente.");
        }

        model.addAttribute("user", new User());
        return "login";  // Devuelve la vista de login
    }

}
