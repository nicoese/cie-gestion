package com.example.demo.inscripcion;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("inscripcion")
@AllArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final CursoService cursoService;


//    @PostMapping
//    public Inscripcion nuevaInscripcion(@RequestBody Inscripcion inscripcion) {
//        ///TODO: validar y rellenar con datos de alumno y curso
//        return inscripcionService.createInscripcion(inscripcion);
//    }

    @GetMapping("{inscripcionId}")
    public Optional<Inscripcion> getInscripcion(@PathVariable("inscripcionId") String inscripcionId) {
        ///TODO: validar
        return inscripcionService.readInscripcion(inscripcionId);
    }

    @PutMapping("{inscripcionId}")
    public Inscripcion modificarInscripcion(@RequestBody Inscripcion inscripcion,
                                            @PathVariable("inscripcionId") String inscripcionId) {
        ///TODO: validar
        return inscripcionService.updateInscripcion(inscripcion, inscripcionId);
    }

    @DeleteMapping("{inscripcionId}")
    public void eliminarInscripcion(@PathVariable("inscripcionId") String inscripcionId) {
        inscripcionService.deleteInscripcion(inscripcionId);
    }

    @GetMapping("enlistar")
    public String enlistarGet(Model model){
        model.addAttribute("newInscripcion", new Inscripcion());
        model.addAttribute("cursosForInscripcion", cursoService.getAll());
        return "admin";
    }

    @PostMapping("enlistar")
    public String enlistar(String userId,
                           String cursoId,
                           Model model) throws ExcepcionServicio {
       Inscripcion inscripcion = inscripcionService.enlistar(userId, cursoId);
       model.addAttribute("inscripcion", inscripcion);
        return "admin";
    }

}
