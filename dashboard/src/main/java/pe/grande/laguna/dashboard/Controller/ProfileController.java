package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Notification;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.NotificationRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    final UsersRepository usersRepository;
    final MicroNetworkRepository microNetworkRepository;
    private final NotificationRepository notificationRepository;

    public ProfileController(UsersRepository usersRepository, MicroNetworkRepository microNetworkRepository,
                             NotificationRepository notificationRepository) {
        this.usersRepository = usersRepository;
        this.microNetworkRepository = microNetworkRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/profileUser/{id}")
    public String profile(Model model, @PathVariable("id") String id) { //En este perfil se colocarán las opciones de alertas y notificaciones en una sección con los datos
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        model.addAttribute("user", user);

        //Extraer los ids de los micronetworks del usuario
        List<String> listaIdsMicronetwork = user.getMicronetworkList();

        List<Notification> listaNotificaciones = new ArrayList<>();

        for (String idMicronetwork : listaIdsMicronetwork) {
            List<Notification> notificacionesDeEstaMicrored =
                    notificationRepository.findByIdMicronetwork(idMicronetwork);

            // Añadirlas a la lista general
            listaNotificaciones.addAll(notificacionesDeEstaMicrored);
        }

        model.addAttribute("listaNotificaciones", listaNotificaciones);

        return "profile";
    }

    @GetMapping("/profileAdmin")
    public String profileAdmin(Model model) { //se colocan datos de la cuenta y las notificaciones

        //Al superadmin le deben llegar todas las notificaciones de todas las microredes
        List<Notification> listaNotificaciones = notificationRepository.findAll();
        model.addAttribute("listaNotificaciones", listaNotificaciones);


        return "profileAdmin";
    }
}
