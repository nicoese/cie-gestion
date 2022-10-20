package com.example.demo.email;

import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender;
    private UserService userService;

    public void sendPasswordEmail(String userId, String pw) throws ExcepcionServicio {

        AppUser user = userService.getOne(userId)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );

        String subject = "Ya podes iniciar tu sesion!";
        String body = "hola, " + user.getAlumno().getNombreCompleto() +
                " aca esta tu contrasena " + pw;

        this.sendEmail(user.getEmail(), subject, body);

    }
    @Async
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        message.setFrom("emailsinstitutocie@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);

        System.out.println("mail enviado");
    }


}
