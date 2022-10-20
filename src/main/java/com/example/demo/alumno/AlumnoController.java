package com.example.demo.alumno;

import com.example.demo.curso.Curso;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.inscripcion.Inscripcion;
import com.example.demo.modulo.ModuloController;
import com.example.demo.modulo.RequestAlumnoIdAndCurso;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.html.HTMLElement;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.modulo.ModuloController.*;

@Controller
@RequestMapping("alumno")
@AllArgsConstructor
public class AlumnoController {

    private AlumnoService alumnoService;
    private final UserService userService;

    @PostMapping
    public AlumnoApp crearNuevoAlumno(@RequestBody AlumnoApp alumnoApp) throws ExcepcionServicio {
        ///TODO: validaciones
        return alumnoService.createAlumno(alumnoApp);
    }

    @GetMapping
    public List<AlumnoApp> getAlumnos() {
        return alumnoService.getAlumnos();
    }

    @GetMapping("/{alumnoId}")
    public Optional<AlumnoApp> getAlumno(@PathVariable("alumnoId") String alumnoId) {
        ///TODO: validaciones
        return alumnoService.readAlumno(alumnoId);
    }

//    @PutMapping("/{alumnoId}")
//    public AlumnoApp modificarAlumno(@RequestBody AlumnoApp alumno,
//                                     @PathVariable("alumnoId") String alumnoId) {
//        ///TODO: validaciones
//        return alumnoService.updateAlumno(alumno, alumnoId);
//    }

    @DeleteMapping("/{alumnoId}")
    public void eliminarAlumno(@PathVariable("alumnoId") String alumnoId) {
        alumnoService.deleteAlumno(alumnoId);
    }

    @PostMapping("/{alumnoId}")
    public AlumnoApp bajaAlumno(@PathVariable("alumnoId") String alumnoId) {
        return alumnoService.bajaAlumno(alumnoId);
    }

    @PostMapping("/setcurso/{cursoId}")
    public AppUser setCurso(@PathVariable("cursoId") String cursoId,
                            @RequestBody String userDni)
            throws ExcepcionServicio {
        return alumnoService.setCurso(userDni, cursoId);
    }

    @GetMapping("/cursosAlumno/{alumnoId}")
    public String encontrarCursos(@PathVariable("alumnoId") String alumnoId, Model model) {
        List<Curso> cursos = alumnoService.findCursos(alumnoId);
        Collections.sort(cursos);
//        model.addAttribute("cursoId", "");
        model.addAttribute("alumnoIdx", alumnoId);
        model.addAttribute("cursos", cursos);
        model.addAttribute("cursox", new RequestAlumnoIdAndCurso());

        return "listaCursos";
    }

    @GetMapping("buscar")
    public String buscar(Model model) {
        model.addAttribute("buscarxdni", "alksjdfal");
        return "admin";
    }

    @GetMapping("datos")
    public String datos(String username,
                        Model model) {
        try {
            model.addAttribute("userForBaja",
                    userService.findByUsername(username)
                            .orElseThrow(() -> new IllegalStateException("Usuario con DNI " + username + " no encontrado")));

        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "admin";
    }

    @PostMapping("baja-alta")
    public String bajaAlta(Boolean bajaAlta,
                           String user,
                           Model model) {

        if (bajaAlta) alumnoService.altaAlumno(user);
        if (!bajaAlta) alumnoService.bajaAlumno(user);
        model.addAttribute("mensaje", "Alumno actualizado correctamente");


        return "admin";
    }


}


