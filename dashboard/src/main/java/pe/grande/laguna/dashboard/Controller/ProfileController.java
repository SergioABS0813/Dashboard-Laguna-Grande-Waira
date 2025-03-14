package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    final UsersRepository usersRepository;
    final MicroNetworkRepository microNetworkRepository;

    public ProfileController(UsersRepository usersRepository, MicroNetworkRepository microNetworkRepository) {
        this.usersRepository = usersRepository;
        this.microNetworkRepository = microNetworkRepository;
    }

    @GetMapping("/profileUser/{id}")
    public String profile(Model model, @PathVariable("id") String id) { //En este perfil se colocarán las opciones de alertas y notificaciones en una sección con los datos
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        model.addAttribute("user", user);

        //Crear micronetworkList
        ArrayList<MicroNetwork> microNetworkArrayList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();

        //Solo para probar datatable
        model.addAttribute("microNetworkList", microNetworkArrayList);

        return "profile";
    }

    @GetMapping("/profileAdmin")
    public String profileAdmin(Model model) { //se colocan datos de la cuenta y las notificaciones

        return "profileAdmin";
    }
}
