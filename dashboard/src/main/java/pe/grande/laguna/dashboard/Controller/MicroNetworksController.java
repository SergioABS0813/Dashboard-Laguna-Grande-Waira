package pe.grande.laguna.dashboard.Controller;


import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.MicroNetworkRepository;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class MicroNetworksController {

    final MicroNetworkRepository microNetworkRepository;
    final SettingsRepository settingsRepository;
    final UsersRepository usersRepository;

    public MicroNetworksController(MicroNetworkRepository microNetworkRepository, SettingsRepository settingsRepository, UsersRepository usersRepository) {
        this.microNetworkRepository = microNetworkRepository;
        this.settingsRepository = settingsRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/micronetworks")
    public String table(Model model) {

        System.out.println("Comenzando micronetworks");

        Settings userSettings = new Settings();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal()) && auth.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {

            org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            String email = securityUser.getUsername();

            User userEntity = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!userEntity.getRole().equals("ROLE_ADMIN")) {
                userSettings = settingsRepository.findByAdminId(userEntity.getId());
            }
        }

        ArrayList<MicroNetwork> microNetworkList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();
        model.addAttribute("settings", userSettings);
        model.addAttribute("microNetworkList", microNetworkList);
        model.addAttribute("mapMarkersData", microNetworkList);

        return "micronetworks/table_micronetworks";
    }

    /* ********** START: Crear micronetworks ********** */

    @GetMapping("/micronetworks/create")
    public String add(Model model) {

        model.addAttribute("microNetwork", new MicroNetwork());
        return "micronetworks/add_micronetwork";
    }

    @PostMapping("/micronetworks/create")
    public String create(@Valid @ModelAttribute("microNetwork") MicroNetwork microNetwork, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        // Si existen errores de validación, se retorna a la vista del formulario.
        if (bindingResult.hasErrors()) {
            // Los errores se mostrarán en la misma vista
            return "micronetworks/add_micronetwork";
        }

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

        // Flash Attribute
        redirectAttributes.addFlashAttribute("successMessageCreate", "Microred creada correctamente");

        return "redirect:/micronetworks";
    }

    /* ********** END: Crear micronetworks ********** */

    /* ********** START: Editar micronetworks ********** */

    @GetMapping("/micronetworks/edit/{id}")
    public String editMicroNetwork(@PathVariable("id") String id, Model model) {
        // Buscamos la microred por ID en la base de datos
        MicroNetwork microNetwork = microNetworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MicroNetwork no encontrada con id: " + id));

        // Agregamos al modelo para que Thymeleaf rellene el formulario
        model.addAttribute("microNetwork", microNetwork);

        // Retornar la vista del formulario
        return "micronetworks/edit_micronetwork";
    }

    @PostMapping("/micronetworks/edit")
    public String updateMicroNetwork(
            @Valid @ModelAttribute("microNetwork") MicroNetwork microNetwork,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Analizamos si hay errores primero
        if (bindingResult.hasErrors()) {
            // Si hay errores, regresamos al formulario de edición
            return "micronetworks/edit_micronetwork";
        }

        // Buscar la microred original en la BD
        MicroNetwork existing = microNetworkRepository.findById(microNetwork.getId())
                .orElseThrow(() -> new RuntimeException("No se encontró la microred con ID: " + microNetwork.getId()));

        // Actualizar los campos que permites modificar
        existing.setAlias(microNetwork.getAlias());
        existing.setLat(microNetwork.getLat());
        existing.setLon(microNetwork.getLon());
        existing.setSiteVRM(microNetwork.getSiteVRM());
        existing.setSiteWeatherLink(microNetwork.getSiteWeatherLink());
        existing.setSiteSparkMeter(microNetwork.getSiteSparkMeter());
        existing.setStatus(microNetwork.getStatus());

        //Definir la zona horaria de Perú
        ZoneId limaZone = ZoneId.of("America/Lima");

        //Obtener la fecha/hora actual en esa zona
        LocalDateTime localNow = LocalDateTime.now(limaZone);

        // Convertir LocalDateTime -> Instant -> Date
        Instant instant = localNow.atZone(limaZone).toInstant();
        Date peruDate = Date.from(instant);

        existing.setTimeEdition(peruDate); // Actualizamos la fecha de edición

        // Guardar cambios
        microNetworkRepository.save(existing);

        // Flash Attribute
        redirectAttributes.addFlashAttribute("successMessageEdit", "Microred editada correctamente");

        // Redirigir a la tabla de microredes
        return "redirect:/micronetworks";
    }









}
