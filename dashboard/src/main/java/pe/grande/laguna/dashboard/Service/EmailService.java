package pe.grande.laguna.dashboard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHelloEmail(String correoReceptor) {
        SimpleMailMessage message = new SimpleMailMessage();
        // La cuenta emisora del correo se configura en el application properties
        message.setTo(correoReceptor);
        message.setSubject("Hola desde Spring Boot"); // Asunto del mensaje
        message.setText("¡Hola! Este es un mensaje de prueba enviado desde Spring Boot usando Gmail SMTP."); //Mensaje

        mailSender.send(message);
    }
}
