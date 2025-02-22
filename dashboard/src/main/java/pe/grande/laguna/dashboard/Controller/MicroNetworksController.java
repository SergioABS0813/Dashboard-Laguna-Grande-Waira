package pe.grande.laguna.dashboard.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

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
        model.addAttribute("microNetwork", new MicroNetwork());
        return "micronetworks/add_micronetwork";
    }

    @PostMapping("/micronetworks/create")
    public String create(@ModelAttribute("microNetwork") MicroNetwork microNetwork) {
        //Definir la zona horaria de Perú
        ZoneId limaZone = ZoneId.of("America/Lima");

        //Obtener la fecha/hora actual en esa zona
        LocalDateTime localNow = LocalDateTime.now(limaZone);

        // Convertir LocalDateTime -> Instant -> Date
        Instant instant = localNow.atZone(limaZone).toInstant();
        Date peruDate = Date.from(instant);

        // Asignar a las propiedades a microNetwork
        microNetwork.setTimeCreation(peruDate);
        microNetwork.setTimeEdition(peruDate);

        // Agregamos el status: ACTIVO por default
        microNetwork.setStatus("ACTIVO");

        // Guardamos en DB :D
        microNetworkRepository.save(microNetwork);

        return "redirect:/micronetworks";
    }



}
