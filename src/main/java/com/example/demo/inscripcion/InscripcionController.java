package com.example.demo.inscripcion;

import com.example.demo.comision.Comision;
import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.enums.Turno;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("inscripcion")
@AllArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final CursoService cursoService;

    @GetMapping
    public String getAllInscripciones(Model model) {
        model.addAttribute("inscripciones", inscripcionService.getAllInscripciones());
        model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());
        return "admin";
    }

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
    public String enlistarGet(Model model) {
        model.addAttribute("newInscripcion", new Inscripcion());
        model.addAttribute("cursosForInscripcion", cursoService.getAll());
        return "admin";
    }

    @PostMapping("enlistar")
    public String enlistar(String userId,
                           String cursoId,
                           Turno turno,
                           Comision comision,
                           Model model) throws ExcepcionServicio {
        Inscripcion inscripcion = inscripcionService
                .enlistar(userId, cursoId, turno, comision);
        model.addAttribute("inscripcion", inscripcion);
        model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());

        return "admin";
    }

    @GetMapping("buscar")
    public String buscar(String cadena,
                         Model model) {
        try {
            model.addAttribute("inscripciones",
                    inscripcionService.buscarInscripciones(cadena));
            model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());

        }
        return "admin";
    }

    @GetMapping("filtrar")
    public String filtrarInscripciones(RequestFilterInscripcion requestFilterInscripcion,
                                       Model model) {
        try {
            model.addAttribute("inscripciones", inscripcionService.filtrarInscripciones(
                    requestFilterInscripcion.nombreAdmin,
                    requestFilterInscripcion.turno,
                    requestFilterInscripcion.inscripciones));
            model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());

        }catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("requestFilterInscripcion", new RequestFilterInscripcion());
            model.addAttribute("inscripciones", inscripcionService.getAllInscripciones());
        }
        return "admin";
    }

}
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
class RequestFilterInscripcion{
    String nombreAdmin;
    Turno turno;
    List<Inscripcion> inscripciones;
}