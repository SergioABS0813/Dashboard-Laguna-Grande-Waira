package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;
import pe.grande.laguna.dashboard.Service.VRMService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {

    private final UsersRepository usersRepository;
    private final MicroNetworkRepository microNetworkRepository;
    private final SettingsRepository settingsRepository;
    private final VRMService VRMService;

    public DashboardController(UsersRepository usersRepository, MicroNetworksController microNetworksController, MicroNetworkRepository microNetworkRepository, SettingsRepository settingsRepository, VRMService VRMService) {
        this.usersRepository = usersRepository;
        this.microNetworkRepository = microNetworkRepository;
        this.settingsRepository = settingsRepository;
        this.VRMService = VRMService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @RequestParam(name = "instalation", required = false, defaultValue = "0") int instalation){

        Optional<Settings> OptionalSettings = settingsRepository.findAll().stream().findFirst();
        if (OptionalSettings.isPresent()) {
            Settings userSettings = OptionalSettings.get();
            model.addAttribute("settings", userSettings);
        } else {
            model.addAttribute("settings", null);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {

            org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            String email = securityUser.getUsername();

            User userEntity = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (userEntity != null) {

                ArrayList<String> microNetworksIds = userEntity.getMicronetworkList();

                ArrayList<MicroNetwork> microNetworkList = new ArrayList<>();
                for (String microNetworkId : microNetworksIds) {

                    Optional<MicroNetwork> microNetworkOptional = microNetworkRepository.findById(microNetworkId);
                    microNetworkOptional.ifPresent(microNetworkList::add);
                }

                if (instalation >= 0 && instalation < microNetworkList.size()) {

                    System.out.println("Instalacion Elegida: " + instalation);
                    model.addAttribute("selectedMicroNetwork", microNetworkList.get(instalation));
                    model.addAttribute("microNetworkList", microNetworkList);





                } else {
                    model.addAttribute("selectedMicroNetwork", null);
                }

            } else {
                model.addAttribute("microNetworkList", null);
                model.addAttribute("selectedMicroNetwork", null);
            }
        }

        return "dashboard";
    }


}
