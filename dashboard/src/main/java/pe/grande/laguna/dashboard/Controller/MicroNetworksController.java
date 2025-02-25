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
import pe.grande.laguna.dashboard.Service.TokenValidationService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Controller
public class MicroNetworksController {

    final MicroNetworkRepository microNetworkRepository;
    final SettingsRepository settingsRepository;
    final UsersRepository usersRepository;
    private final TokenValidationService tokenValidationService;

    public MicroNetworksController(MicroNetworkRepository microNetworkRepository, SettingsRepository settingsRepository, UsersRepository usersRepository, TokenValidationService tokenValidationService) {
        this.microNetworkRepository = microNetworkRepository;
        this.settingsRepository = settingsRepository;
        this.usersRepository = usersRepository;
        this.tokenValidationService = tokenValidationService;
    }

    @GetMapping("/micronetworks")
    public String table(Model model) {

        System.out.println("Comenzando micronetworks");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {

            org.springframework.security.core.userdetails.User securityUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            String email = securityUser.getUsername();

            User userEntity = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            boolean isAdmin = userEntity.getRole().equals("ADMIN");
            System.out.println("isAdmin: " + isAdmin);

            if (isAdmin) {

                ArrayList<MicroNetwork> microNetworkList = (ArrayList<MicroNetwork>) microNetworkRepository.findAll();
                model.addAttribute("microNetworkList", microNetworkList);
                model.addAttribute("mapMarkersData", microNetworkList);

            } else {

                ArrayList<String> microNetworksIds = userEntity.getMicronetworkList();

                ArrayList<MicroNetwork> microNetworkList = new ArrayList<>();
                for (String microNetworkId : microNetworksIds) {

                    Optional<MicroNetwork> microNetworkOptional = microNetworkRepository.findById(microNetworkId);
                    microNetworkOptional.ifPresent(microNetworkList::add);
                }

                model.addAttribute("microNetworkList", microNetworkList);
                model.addAttribute("mapMarkersData", microNetworkList);
            }
            model.addAttribute("isAdmin", isAdmin);
        }

        return "micronetworks/table_micronetworks";
    }

    /* ********** START: Crear micronetworks ********** */

    @GetMapping("/micronetworks/create")
    public String add(Model model, RedirectAttributes redirectAttributes) {

        Settings settings = settingsRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay Settings en la BD"));

        //Validamos

        boolean weatherOk = tokenValidationService.validateWeatherLink(
                settings.getKeyWeatherLink(),
                settings.getSecretWeatherLink()
        );
        boolean sparkOk = tokenValidationService.validateSparkMeter(
                settings.getKeySparkMeter(),
                settings.getSecretSparkMeter()
        );
        boolean vrmOk = tokenValidationService.validateVRM(settings.getTokenVRM());

        if (!weatherOk) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "El token/key de WeatherLink es inválido. Por favor, verifique los valores en Ajustes.");
            return "redirect:/micronetworks";
        } else if (!sparkOk) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "El token/key de Sparkmeter es inválido. Por favor, verifique los valores en Ajustes.");
            return "redirect:/micronetworks";
        } else if (!vrmOk) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "El token de VRM es inválido. Por favor, verifique el valor en Ajustes.");
            return "redirect:/micronetworks";

        }

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

    /* ********** END: Editar micronetworks ********** */


    /* ********** START: Eliminar micronetworks ********** */

    @GetMapping("/micronetworks/delete/{id}")
    public String deleteMicroNetwork(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        // Buscar la microred por ID, o lanzar excepción si no existe
        MicroNetwork microNetwork = microNetworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la microred con ID: " + id));

        // Eliminar la microred
        microNetworkRepository.delete(microNetwork);

        // Agregar un mensaje flash de éxito
        redirectAttributes.addFlashAttribute("successMessageDelete", "Microred eliminada correctamente");

        // Redirigir a la tabla de microredes
        return "redirect:/micronetworks";
    }

    /* ********** END: Eliminar micronetworks ********** */

    /* ********** START: View Micronetworks ********** */

    @GetMapping("/micronetworks/view/{id}")
    public String viewMicronetwork(@PathVariable("id") String id, Model model){
        // Buscar la microred por ID, o lanzar excepción si no existe
        MicroNetwork microNetwork = microNetworkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la microred con ID: " + id));

        //mandamos el micronetwork a la vista
        model.addAttribute("microNetwork", microNetwork);
        return "dashboard";

    }





    /* ********** END: View Micronetworks ********** */










}
