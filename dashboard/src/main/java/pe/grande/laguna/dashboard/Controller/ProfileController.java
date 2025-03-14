package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.ArrayList;

@Controller
public class ProfileController {

    final UsersRepository usersRepository;

    public ProfileController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/profileUser/{id}")
    public String profile(Model model, @PathVariable("id") String id) { //En este perfil se colocarán las opciones de alertas y notificaciones en una sección con los datos
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profileAdmin")
    public String profileAdmin(Model model) { //se colocan datos de la cuenta y las notificaciones

        return "profileAdmin";
    }
}
