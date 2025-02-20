package pe.grande.laguna.dashboard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

}
