package pe.grande.laguna.dashboard.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Dto.UserSettingsDto;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Controller
public class SettingsController {

    final SettingsRepository settingsRepository;
    final UsersRepository usersRepository;

    public SettingsController(SettingsRepository settingsRepository, UsersRepository usersRepository) {
        this.settingsRepository = settingsRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/settings")
    public String settings(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            org.springframework.security.core.userdetails.User securityUser =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            String email = securityUser.getUsername();

            User userEntity = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Optional optuserSettings = settingsRepository.findById("67baa0ad113e5fa280a2aafa");
            Settings userSettings = (Settings) optuserSettings.get();



            // Crear el DTO que agrupa User y Settings
            UserSettingsDto userSettingsDto = new UserSettingsDto(userEntity, userSettings);
            model.addAttribute("userSettingsDto", userSettingsDto);

            return "settings";
        }
        return "redirect:/login";
    }


    @PostMapping("/settings/edit")
    public String editSettings(@Valid @ModelAttribute("userSettingsDto") UserSettingsDto userSettingsDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            System.out.println("Error encontrado:");
            // Imprime todos los errores en consola
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println("Error en validación: " + error);
            });

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {

                return "redirect:/settings"; //Nos manda sí o sí a settings nuevamente
            }
            return "redirect:/login";

        }

        // Extraer y actualizar User
        User updatedUser = userSettingsDto.getUser();
        System.out.println(updatedUser.isAlertsEmail());
        System.out.println(updatedUser.isAlertsSMS());
        System.out.println(updatedUser.isAlertsWhatsapp());
        // Aquí puedes aplicar lógicas de actualización, e.g.:
        usersRepository.save(updatedUser);

        // Extraer y actualizar Settings
        Settings updatedSettings = userSettingsDto.getSettings();

        //Definir la zona horaria de Perú
        ZoneId limaZone = ZoneId.of("America/Lima");

        //Obtener la fecha/hora actual en esa zona
        LocalDateTime localNow = LocalDateTime.now(limaZone);

        // Convertir LocalDateTime -> Instant -> Date
        Instant instant = localNow.atZone(limaZone).toInstant();
        Date peruDate = Date.from(instant);

        updatedSettings.setTimeEdition(peruDate);
        settingsRepository.save(updatedSettings);

        redirectAttributes.addFlashAttribute("successMessage", "Datos actualizados correctamente");
        return "redirect:/settings";
    }


}
