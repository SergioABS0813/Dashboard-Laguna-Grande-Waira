package pe.grande.laguna.dashboard.Controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class AdminController {

    final UsersRepository usersRepository;
    final MicroNetworkRepository microNetworkRepository;
    final PasswordEncoder passwordEncoder;

    public AdminController(UsersRepository usersRepository, MicroNetworkRepository microNetworkRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.microNetworkRepository = microNetworkRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users_managment")
    public String table(Model model) {

        ArrayList<User> userList = (ArrayList<User>) usersRepository.findAll();
        model.addAttribute("userList", userList);

        return "users_managment/table_users";
    }

    /* ******** START: Eliminar Usuario ******** */

    @GetMapping("/user_managment/delete/{id}")
    public String deleteUser(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        // 1. Buscar usuario por ID
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // 2. Eliminarlo
        usersRepository.delete(user);

        // 3. Mensaje de éxito (flash attribute)
        redirectAttributes.addFlashAttribute("successMessageDelete", "Usuario eliminado correctamente");

        // 4. Redirigir a la lista de usuarios
        return "redirect:/users_managment";
    }

    /* ******** END: Eliminar Usuario ******** */

    /* ******** START: Crear Usuario ******** */
    @GetMapping("/user_managment/create")
    public String add(Model model) {
        // Se envía la lista de micronetworks
        ArrayList<MicroNetwork> listaInstalaciones = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();
        model.addAttribute("listaInstalaciones", listaInstalaciones);

        // Se envía un nuevo objeto User vacío
        model.addAttribute("user", new User());
        return "users_managment/add_user";
    }

    @PostMapping("/user_managment/create")
    public String createUser(
            @ModelAttribute("user") User user,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Si tuvieras validaciones con @Valid, revisa si hay errores:
        if (bindingResult.hasErrors()) {
            return "users_managment/add_user"; // vuelve al formulario
        }

        // Ajusta campos por defecto, si aplica:
        user.setRole("USER");
        user.setActive(true);

        //Definir la zona horaria de Perú
        ZoneId limaZone = ZoneId.of("America/Lima");

        //Obtener la fecha/hora actual en esa zona
        LocalDateTime localNow = LocalDateTime.now(limaZone);

        // Convertir LocalDateTime -> Instant -> Date
        Instant instant = localNow.atZone(limaZone).toInstant();
        Date peruDate = Date.from(instant);
        user.setCreationTime(peruDate);

        // Ejemplo: si quisieras encriptar la contraseña (opcional)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar en MongoDB
        usersRepository.save(user);

        // Agregar un mensaje de éxito (opcional)
        redirectAttributes.addFlashAttribute("successMessageUser", "Usuario creado exitosamente");

        // Redirigir a alguna lista de usuarios, o donde gustes
        return "redirect:/users_managment";
    }




    /* ******** END: Crear Usuario ******** */


}
