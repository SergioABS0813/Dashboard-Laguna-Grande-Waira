package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.ArrayList;

@Controller
public class ProfileController {

    final UsersRepository usersRepository;

    public ProfileController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/profileUser")
    public String profile(Model model) { //En este perfil se colocarán las opciones de alertas y notificaciones en una sección con los datos


        return "profile";
    }

    @GetMapping("/profileAdmin")
    public String profileAdmin(Model model) { //se colocan datos de la cuenta y las notificaciones

        return "profileAdmin";
    }
}
