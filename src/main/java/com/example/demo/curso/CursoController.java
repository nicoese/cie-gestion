package com.example.demo.curso;

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
    public Curso crearNuevoCurso(@RequestBody Curso curso){
        ///TODO: validar
        return cursoService.createCurso(curso);
    }

    @GetMapping
    public List<Curso> getAllCursos(){
        return cursoService.getAll();
    }

    @GetMapping("{cursoId}")
    public Optional<Curso> getCurso(@PathVariable("cursoId")String cursoId){
        ///TODO: validar
        return cursoService.readCurso(cursoId);
    }

    @PutMapping("{cursoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Curso modificarCurso(@RequestBody Curso curso, @PathVariable("cursoId")String cursoId){
        ///TODO: validar
        return cursoService.updateCurso(curso, cursoId);
    }

    @DeleteMapping("{cursoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void eliminarCurso(@PathVariable("cursoId")String cursoId){
        cursoService.deleteCurso(cursoId);
    }

    @PostMapping("{cursoId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Curso bajaCurso(@PathVariable("cursoId")String cursoId){
        ///TODO: validar
        return cursoService.bajaCurso(cursoId);
    }

    @GetMapping("crear-curso")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearCurso(Model model){
        model.addAttribute("newCurso", new Curso());
        return "admin";
    }

    @PostMapping("crear-curso")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String persistirCurso(Curso curso,
                                 Model model){
        cursoService.createCurso(curso);
        return "admin";
    }

}
