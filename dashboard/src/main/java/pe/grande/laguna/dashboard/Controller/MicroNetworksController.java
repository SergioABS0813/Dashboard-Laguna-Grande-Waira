package pe.grande.laguna.dashboard.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;

import java.util.ArrayList;

@Controller
public class MicroNetworksController {

    final MicroNetworkRepository microNetworkRepository;

    public MicroNetworksController(MicroNetworkRepository microNetworkRepository) {
        this.microNetworkRepository = microNetworkRepository;
    }

    @GetMapping("/micronetworks")
    public String table(Model model) {

        ArrayList<MicroNetwork> microNetworkList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();
        model.addAttribute("microNetworkList", microNetworkList);
        model.addAttribute("mapMarkersData", microNetworkList);

        return "micronetworks/table_micronetworks";
    }

    @GetMapping("/micronetworks/create")
    public String add(Model model) {

        ArrayList<MicroNetwork> microNetworkList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();
        model.addAttribute("microNetworkList", microNetworkList);

        return "micronetworks/add_micronetwork";
    }

}
