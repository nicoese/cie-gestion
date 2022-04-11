package com.example.demo.modulo;

import com.example.demo.alumno.AlumnoService;
import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.excepciones.ExcepcionControlador;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.nota.Nota;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("modulo")
@AllArgsConstructor
public class ModuloController {

    private final ModuloService moduloService;
    private final AlumnoService alumnoService;
    private final CursoService cursoservice;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Modulo crearNuevoModulo(@RequestBody Modulo modulo) {
        ///TODO: validar
        return moduloService.createModulo(modulo);
    }

    @GetMapping("{moduloId}")
    public Optional<Modulo> getModulo(@PathVariable("moduloId") String moduloId) {
        ///TODO: validar
        return moduloService.readModulo(moduloId);
    }

    @PutMapping("{moduloId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Modulo modificarModulo(@RequestBody Modulo modulo, @PathVariable("moduloId") String moduloId) {
        ///TODO: validar
        return moduloService.updateModulo(modulo, moduloId);
    }

    @DeleteMapping("{moduloId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void eliminarModulo(@PathVariable("moduloId") String moduloId) {
        moduloService.deleteModulo(moduloId);
    }

    @PostMapping("{moduloId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Modulo bajaModulo(@PathVariable("moduloId") String moduloId) {
        return moduloService.bajaModulo(moduloId);
    }

    @GetMapping("/vista/{alumnoId}")
    public String modulo(
            @RequestParam(name = "cursoId") String cursoId,
            @PathVariable("alumnoId") String alumnoId,
            Model model) throws ExcepcionServicio, ExcepcionControlador {

        System.out.println(cursoId);

//        RequestAlumnoIdAndCurso requestAlumnoIdAndCurso =
//                new RequestAlumnoIdAndCurso(
//                        alumnoId, cursoId
//                );
////        String cursoId = curso.getId();
//        System.out.println("id curso: " + requestAlumnoIdAndCurso.getAlumnoId());
//        System.out.println(requestAlumnoIdAndCurso.getCursoId());
//
        List<Nota> notas = alumnoService.findNotasByCurso(cursoId, alumnoId);
//
        model.addAttribute("alumno", alumnoService.readAlumno(alumnoId)
                .orElseThrow(
                        () -> new ExcepcionControlador("alumno no encontrado")
                ));
        model.addAttribute("curso", cursoservice.readCurso(cursoId)
                .orElseThrow(
                        () -> new ExcepcionControlador("curso no encontrado")
                ));
        model.addAttribute("notas", notas);


        return "modulo";
    }

    @GetMapping("crear")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearModulo(Model model){
        model.addAttribute("newModulo", new Modulo());
        model.addAttribute("cursosForNewModulo", cursoservice.getAll());
        return "admin";
    }

    @PostMapping("crear")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String crearMod(Modulo modulo,
                           Model model){
        moduloService.createModulo(modulo);
        return "admin";
    }


}


