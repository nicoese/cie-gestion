package com.example.demo.curso;

import com.example.demo.enums.CursoLista;
import com.example.demo.excepciones.ExcepcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("curso")
@AllArgsConstructor
public class CursoController {

    private CursoService cursoService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Curso crearNuevoCurso(@RequestBody Curso curso) {
        ///TODO: validar
        return cursoService.createCurso(curso);
    }

    @GetMapping
    public List<Curso> getAllCursos() {
        return cursoService.getAll();
    }

    @GetMapping("{cursoId}")
    public Optional<Curso> getCurso(@PathVariable("cursoId") String cursoId) {
        ///TODO: validar
        return cursoService.readCurso(cursoId);
    }

    @DeleteMapping("{cursoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void eliminarCurso(@PathVariable("cursoId") String cursoId) {
        cursoService.deleteCurso(cursoId);
    }

    @PostMapping("{cursoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Curso bajaCurso(@PathVariable("cursoId") String cursoId) {
        ///TODO: validar
        return cursoService.bajaCurso(cursoId);
    }

    @GetMapping("crear")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearCurso(Model model) {
        model.addAttribute("newCurso", new Curso());
        return "admin";
    }

    @PostMapping("crear")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String persistirCurso(Curso curso,
                                 Model model) {
        try {
            cursoService.createCurso(curso);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("newCurso", new Curso());
            model.addAttribute("url", "crear-curso");
        }
        return "admin";
    }

    @GetMapping("modificar-curso")
    public String modificarCurso(Model model) {
        model.addAttribute("modCurso", new Curso());
        model.addAttribute("cursosForModCurso", cursoService.getAll());
        return "admin";
    }

    @PostMapping("modificar-curso")
    public String modificarCursoPost(Model model,
                                     Curso curso) {
        try {
            cursoService.updateCurso(curso, curso.getId());
            model.addAttribute("modificacionRealizada", "Modificacion realizada exitosamente!");
            model.addAttribute("modCurso", new Curso());
            model.addAttribute("cursosForModCurso", cursoService.getAll());
        } catch (ExcepcionServicio exceptionServicio) {
            model.addAttribute("url", "modificar-curso");
            model.addAttribute("modCurso", new Curso());
            model.addAttribute("cursosForModCurso", cursoService.getAll());
            model.addAttribute("error", exceptionServicio.getMessage());
        }
        return "admin";
    }
}
