package pe.grande.laguna.dashboard.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void sendHelloEmailHtml(String correoReceptor) throws MessagingException {
        // 1. Crear el MimeMessage desde mailSender
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // 2. Usar MimeMessageHelper para configurar
        // El segundo parámetro en 'true' indica que permitimos contenido "multipart"
        // y manejo de HTML
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(correoReceptor);
        helper.setSubject("Hola desde Spring Boot (HTML)");

        // 3. Aquí definimos el contenido en HTML.
        // El segundo parámetro 'true' en setText indica que es HTML
        String htmlContent = """
                <html>
                  <body style="font-family: Arial, sans-serif; color: #333;">
                    <h1 style="color: #009688;">¡Hola!</h1>
                    <p>Este es un mensaje de <b>prueba</b> con <i>HTML</i> enviado desde Spring Boot usando Gmail SMTP.</p>
                    <p style="color: #555;">Puedes poner <u>estilos en línea</u> o usar CSS embebido.</p>
                    <hr>
                    <p><em>Saludos desde tu aplicación Spring Boot</em></p>
                  </body>
                </html>
                """;

        helper.setText(htmlContent, true);

        // 4. Enviar el correo
        mailSender.send(mimeMessage);
    }

    /* START Alerta: Medidor Inoperativo */
    public void sendAlertEmailMedidorInoperativo(String correoReceptor,
                                                 String medidorNombre,
                                                 String medidorId,
                                                 String nombreMicrored)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(correoReceptor);
        helper.setSubject("Alerta: Medidor Inoperativo");

        String htmlContent = """
        <html>
          <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
            <table align="center" width="600" style="border-collapse: collapse;">
              <tr>
                <td style="background-color: #d32f2f; color: white; padding: 20px; text-align: center;">
                  <h1 style="margin: 0; font-size: 24px;">Alerta: Medidor Inoperativo</h1>
                </td>
              </tr>
              <tr>
                <td style="padding: 20px; background-color: #ffffff; color: #333333;">
                  <p style="font-size: 16px;">Estimado usuario,</p>
                  <p style="font-size: 16px;">
                    Se ha detectado que el medidor <strong>%s</strong> (ID: <strong>%s</strong>) está inoperativo.
                    <br />
                    Revisar por favor la instalación: <strong>%s</strong>
                  </p>
                  <p style="font-size: 16px;">
                    Por favor, revise el equipo a la brevedad o póngase en contacto con el servicio técnico.
                  </p>
                  <p style="font-size: 16px;">Gracias,</p>
                  <p style="font-size: 16px;"><em>Su equipo de monitoreo</em></p>
                </td>
              </tr>
              <tr>
                <td style="background-color: #f5f5f5; color: #888888; padding: 10px; text-align: center; font-size: 12px;">
                  Este es un correo automático, por favor no responda.
                </td>
              </tr>
            </table>
          </body>
        </html>
        """;

        // Reemplazar placeholders con los datos reales
        htmlContent = String.format(htmlContent, medidorNombre, medidorId, nombreMicrored);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    /* END Alerta: Medidor Inoperativo ---- FALTA QUE DIGA DE QUÉ MICRORED ES LA ALERTA!!!!! */


    /* START Alerta: Batería 46 V o menor significa BATERÍA BAJA */

    public void sendAlertEmailBateriaBaja(String correoReceptor, String nombreMicrored, Double valorVoltajeCritico)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(correoReceptor);
        helper.setSubject("Alerta: Batería del Sistema Baja");

        String htmlContent = """
        <html>
          <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
            <table align="center" width="600" style="border-collapse: collapse;">
              <tr>
                <td style="background-color: #d32f2f; color: white; padding: 20px; text-align: center;">
                  <h1 style="margin: 0; font-size: 24px;">Alerta: Batería del Sistema Baja</h1>
                </td>
              </tr>
              <tr>
                <td style="padding: 20px; background-color: #ffffff; color: #333333;">
                  <p style="font-size: 16px;">Estimado usuario,</p>
                  <p style="font-size: 16px;">
                    Se ha detectado que la batería del sistema se encuentra en un nivel crítico.
                    <br />
                    Revisar por favor la instalación: <strong>%s</strong>.
                    <br />
                    El nivel de voltaje detectado fue: <strong>%s V</strong>.
                  </p>
                  <p style="font-size: 16px;">
                    Por favor, tome las medidas necesarias para solucionar este inconveniente.
                  </p>
                  <p style="font-size: 16px;">Gracias,</p>
                  <p style="font-size: 16px;"><em>Su equipo de monitoreo</em></p>
                </td>
              </tr>
              <tr>
                <td style="background-color: #f5f5f5; color: #888888; padding: 10px; text-align: center; font-size: 12px;">
                  Este es un correo automático, por favor no responda.
                </td>
              </tr>
            </table>
          </body>
        </html>
        """;

        // Reemplaza los placeholders con el nombre de la instalación y el valor del voltaje crítico
        htmlContent = String.format(htmlContent, nombreMicrored, valorVoltajeCritico);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }




    /* END Alerta: Batería 46 V o menor significa BATERÍA BAJA */

    /* START Alerta: Sobrecarga */

    public void sendAlertEmailSobrecarga(String correoReceptor, String nombreMicrored, Double valorConsumo)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(correoReceptor);
        helper.setSubject("Alerta: Sobrecarga en el Sistema");

        String htmlContent = """
        <html>
          <body style="font-family: Arial, sans-serif; margin: 0; padding: 0;">
            <table align="center" width="600" style="border-collapse: collapse;">
              <tr>
                <td style="background-color: #d32f2f; color: white; padding: 20px; text-align: center;">
                  <h1 style="margin: 0; font-size: 24px;">Alerta: Sobrecarga en el Sistema</h1>
                </td>
              </tr>
              <tr>
                <td style="padding: 20px; background-color: #ffffff; color: #333333;">
                  <p style="font-size: 16px;">Estimado usuario,</p>
                  <p style="font-size: 16px;">
                    Se ha detectado que el consumo del sistema está en un nivel de sobrecarga.
                    <br />
                    Por favor, revise la instalación: <strong>%s</strong>.
                    <br />
                    El nivel de consumo detectado fue: <strong>%s</strong>.
                  </p>
                  <p style="font-size: 16px;">
                    Le recomendamos verificar el sistema y tomar las medidas necesarias para evitar posibles daños.
                  </p>
                  <p style="font-size: 16px;">Gracias,</p>
                  <p style="font-size: 16px;"><em>Su equipo de monitoreo</em></p>
                </td>
              </tr>
              <tr>
                <td style="background-color: #f5f5f5; color: #888888; padding: 10px; text-align: center; font-size: 12px;">
                  Este es un correo automático, por favor no responda.
                </td>
              </tr>
            </table>
          </body>
        </html>
        """;

        // Reemplaza los placeholders: %s para el nombre de la instalación y %s para el nivel de consumo
        htmlContent = String.format(htmlContent, nombreMicrored, valorConsumo);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }





    /* END Alerta: Sobrecarga */



}
