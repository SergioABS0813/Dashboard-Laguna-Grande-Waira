package pe.grande.laguna.dashboard.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.grande.laguna.dashboard.Entity.MicroNetwork;
import pe.grande.laguna.dashboard.Entity.Settings;
import pe.grande.laguna.dashboard.Entity.User;
import pe.grande.laguna.dashboard.Repository.SettingsRepository;
import pe.grande.laguna.dashboard.Repository.UsersRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
            // El principal es una instancia de org.springframework.security.core.userdetails.User
            org.springframework.security.core.userdetails.User securityUser =
                    (org.springframework.security.core.userdetails.User) auth.getPrincipal();

            // Suponiendo que el username es el email:
            String email = securityUser.getUsername();

            // Busca en la base de datos tu entidad User por email
            User userEntity = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Settings userSettings = settingsRepository.findByAdminId(userEntity.getId());
            model.addAttribute("settings", userSettings);

            return "settings";
        }

        return "redirect:/login";
    }

    @PostMapping("/settings/edit")
    public String edit(@ModelAttribute("settings") Settings formSettings) {

        Settings existing = settingsRepository.findById(formSettings.getId())
                .orElseThrow(() -> new RuntimeException("No existe Settings con ese ID"));

        existing.setTokenVRM(formSettings.getTokenVRM());
        existing.setTokenWeatherLink(formSettings.getTokenWeatherLink());
        existing.setTokenSparkMeter(formSettings.getTokenSparkMeter());
        existing.setAlertsEmail(formSettings.isAlertsEmail());
        existing.setAlertsWhatsapp(formSettings.isAlertsWhatsapp());
        existing.setAlertsSMS(formSettings.isAlertsSMS());

        //Definir la zona horaria de Perú
        ZoneId limaZone = ZoneId.of("America/Lima");

        //Obtener la fecha/hora actual en esa zona
        LocalDateTime localNow = LocalDateTime.now(limaZone);

        // Convertir LocalDateTime -> Instant -> Date
        Instant instant = localNow.atZone(limaZone).toInstant();
        Date peruDate = Date.from(instant);

        existing.setTimeEdition(peruDate);

        settingsRepository.save(existing);

        return "redirect:/settings";
    }

}
