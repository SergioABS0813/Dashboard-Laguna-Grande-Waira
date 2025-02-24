package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.util.ArrayList;

@Controller
public class AdminController {

    final UsersRepository usersRepository;

    public AdminController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping("/users_managment")
    public String table(Model model) {

        ArrayList<User> userList = (ArrayList<User>) usersRepository.findAll();
        model.addAttribute("userList", userList);

        return "users_managment/table_users";
    }

    @GetMapping("/user_managment/create")
    public String add(Model model) {

        return "users_managment/add_user";
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


}
