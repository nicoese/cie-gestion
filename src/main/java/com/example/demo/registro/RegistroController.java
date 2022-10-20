package com.example.demo.registro;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.alumno.AlumnoService;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.HashMap;

@Controller
@AllArgsConstructor
@RequestMapping("registro")
public class RegistroController {

    private final UserService userService;
    private final RegistroService registroService;
    private final AlumnoService alumnoService;


    @GetMapping("crear-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearUserGet(Model model) {
        model.addAttribute("newUser", new AppUser());
        return "admin";
    }

    @PostMapping("crear-user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearUser(AppUser requestUser,
                            Model model,
                            ModelMap modelMap) {
        try {
            AppUser user = userService.addNewUser(requestUser);
            model.addAttribute("newAlumno", new AlumnoApp());
            model.addAttribute("userId", user.getId());
        } catch (ExcepcionServicio ex) {
            model.addAttribute("newUser", new AppUser());
            modelMap.put("error", ex.getMessage());
        }
        return "admin";
    }

    @PostMapping("crear-alumno/{userId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearAlumno(AlumnoApp alumno,
                              @PathVariable("userId") String userId,
                              String fecha,
                              Model model,
                              ModelMap modelMap) throws ExcepcionServicio, MessagingException {
        try {
            alumno.setFechaNacimiento(LocalDate.parse(fecha));
            registroService.registroAlumno(alumno, userId);
            System.out.println(alumno.getFechaNacimiento());
        } catch (ExcepcionServicio ex) {
            model.addAttribute("newAlumno", new AlumnoApp());
            model.addAttribute("userId", userId);
            modelMap.put("error", ex.getMessage());
        }
        return "admin";
    }

    @GetMapping("modificar-alumno-existente")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String modAlumnoExistente(Model model) {
        model.addAttribute("formbuscar", "a");
        return "admin";
    }

    @GetMapping("form-modificar-alumno")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String modAlumno(String username,
                            Model model,
                            ModelMap modelMap) {
        try {
            model.addAttribute("userAEditar", userService.findByUsername(username)
                    .orElseThrow(() -> new ExcepcionServicio("usuario no encontrado")));
        } catch (ExcepcionServicio e) {
            model.addAttribute("formbuscar", "a");
            modelMap.put("error", e.getMessage());
        }
        return "admin";
    }

    @PostMapping("modificar-alumno")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String modAlumnoPost(AppUser user,
                                Model model,
                                ModelMap map) throws ExcepcionServicio {
        try {
            registroService.editUser(user);
            map.put("usuariomodificado", "usuario modificado exitosamente!");
        } catch (ExcepcionServicio e) {
            model.addAttribute("userAEditar", userService.findByUsername(user.getUsername())
                    .orElseThrow(() -> new ExcepcionServicio("usuario no encontrado")));
            map.put("error", e.getMessage());
        }
        return "admin";
    }

}
