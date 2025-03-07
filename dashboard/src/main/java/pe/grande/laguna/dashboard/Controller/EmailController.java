package pe.grande.laguna.dashboard.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.grande.laguna.dashboard.Service.EmailService;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendHello")
    public String sendHello(RedirectAttributes redirectAttributes) {
        try {
            // Envía el mensaje a tu correo PUCP
            emailService.sendAlertEmailMedidorInoperativo("a20213170@pucp.edu.pe", "MEDIDOR 1", "A0452");
            redirectAttributes.addFlashAttribute("successMessage", "Correo enviado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al enviar el correo: " + e.getMessage());
        }
        return "redirect:/users_managment";
    }
}
