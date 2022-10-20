package com.example.demo.registro;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.alumno.AlumnoService;
import com.example.demo.creacion.CreacionService;
import com.example.demo.email.EmailService;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
public class RegistroService {

    private final AlumnoService alumnoService;
    private final UserService userService;
    private final EmailService emailService;
    private final CreacionService creacionService;


    public void registroAlumno(AlumnoApp alumno, String userId) throws ExcepcionServicio {
        alumnoService.createAlumno(alumno);
        AppUser user = userService.setAlumnoToUser(alumno, userId);
        String pw = userService.generatePW();
        userService.setEncodePW(userId, pw);
        try {
            emailService.sendPasswordEmail(userId, pw);
        } catch (Exception ex) {
            throw new ExcepcionServicio("fallo el envio del email para el usuario." +
                    " Facilite al usuario su contrasena manualmente. Contrasena:" + pw);
        } finally {
            creacionService.infoNuevoAlumno(user.getUsername(), alumno.getNombreCompleto());
        }
    }

    public void editUser(AppUser user) throws ExcepcionServicio {
       try  {
           userService.updateUser(user.getUsername(), user);
           alumnoService.updateAlumno(user.getAlumno().getId(), user.getAlumno());
       }catch (IllegalStateException e){
           throw new ExcepcionServicio(e.getMessage());
       }

    }
}
