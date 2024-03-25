package todocode.hackacode.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import todocode.hackacode.controller.TokenController;

import java.util.Random;

@Component
public class Correo {

    @Autowired
    private MailSender mailSender;



    public void enviarPassword(String destinatario, String nuevaContraTemp) {
        try {
            // Preparar el contenido del mensaje de correo electrónico
            String contenido = "¡Hola! Tu nueva contraseña temporal es: " + nuevaContraTemp;

            // Crear un mensaje de correo electrónico
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject("Recuperación de contraseña");
            message.setText(contenido);

            // Enviar el mensaje de correo electrónico
            mailSender.send(message);

            // Si el mensaje se envía correctamente, podrías agregar un mensaje de registro
            System.out.println("Correo electrónico enviado correctamente.");
        } catch (MailException e) {
            // Manejo de excepciones en caso de que ocurra un error al enviar el correo electrónico
            System.out.println("Error al enviar el correo electrónico: " + e.getMessage());
        } catch (Exception e) {
            // Manejo de excepciones en caso de otros errores
            System.out.println("Error: " + e.getMessage());
        }
    }


    public String generarContraTemp() {
        Random r = new Random();
        StringBuilder contraTemp = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int n = r.nextInt(62);

            if (n <= 9) {
                contraTemp.append((char) (n + '0'));
            } else if (n <= 35) {
                contraTemp.append((char) (n - 10 + 'A'));
            } else {
                contraTemp.append((char) (n - 36 + 'a'));
            }
        }

        return contraTemp.toString();
    }
}
