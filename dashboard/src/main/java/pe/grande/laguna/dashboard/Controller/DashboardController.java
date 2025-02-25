package pe.grande.laguna.dashboard.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping({"/dashboard/{id}"})
    public String dashboard(@PathVariable("id") String id, Model model){

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


            boolean isAdmin = userEntity.getRole().equals("ADMIN");
            System.out.println("isAdmin: " + isAdmin);

            Map<String, MicroNetwork> microNetworkMap = new HashMap<>();

            if (isAdmin) {
                ArrayList<MicroNetwork> microNetworkList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();

                for (MicroNetwork network : microNetworkList) {
                    microNetworkMap.put(network.getId(), network);
                }

            } else {

                ArrayList<String> microNetworksIds = userEntity.getMicronetworkList();
                System.out.println(microNetworksIds.toString());

                for (String microNetworkId : microNetworksIds) {
                    Optional<MicroNetwork> microNetworkOptional = microNetworkRepository.findById(microNetworkId);
                    microNetworkOptional.ifPresent(microNetwork -> microNetworkMap.put(microNetworkId, microNetwork));
                }
            }

            System.out.println("Selected id: " + id);
            MicroNetwork selectedNetwork = microNetworkMap.get(id);

            System.out.println(microNetworkMap.get(id));

            if (selectedNetwork != null) {
                model.addAttribute("selectedMicroNetwork", selectedNetwork);
                System.out.println("Selected MicroNetwork: " + selectedNetwork.getId());
            } else {
                model.addAttribute("selectedMicroNetwork", new ArrayList<>(microNetworkMap.values()).get(0));
                System.out.println("Selected MicroNetwork: " + new ArrayList<>(microNetworkMap.values()).get(0).getId());
            }

            model.addAttribute("microNetworkList", new ArrayList<>(microNetworkMap.values()));

        }

        return "dashboard";
    }


}
