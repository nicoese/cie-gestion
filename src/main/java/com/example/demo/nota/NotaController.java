package com.example.demo.nota;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.enums.EstadoNota;
import com.example.demo.enums.Turno;
import com.example.demo.excepciones.ExcepcionControlador;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.Modulo;
import com.example.demo.modulo.ModuloService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("nota")
@AllArgsConstructor
public class NotaController {

    private NotaService notaService;
    private final CursoService cursoService;
    private final ModuloService moduloService;
    private final NotaRepository notaRepository;

    @GetMapping
    public List<Nota> buscarTodasLasNotas() {
        return notaService.getNotas();
    }

    @GetMapping("{notaId}")
    public Optional<Nota> buscarNota(@PathVariable("notaId") String notaId) {
        return notaService.readNota(notaId);
    }

    @PostMapping
    public Nota crearNuevaNota(@RequestBody Nota nota) {
        return notaService.createNota(nota);
    }

    @DeleteMapping
    public void eliminarNota(String notaId) throws ExcepcionServicio {
        notaService.deleteNota(notaId);
    }

    @GetMapping("curso-modulo")
    public String cursoModulo(Model model) {
        model.addAttribute("cursoModulo", "a");
        model.addAttribute("cursosModulos", cursoService.getAll());
        return "admin";
    }

    @Async
    @ResponseBody
    @GetMapping("cursos-por-modulo/{curso}")
    public List<Modulo> modulosXcurso(@PathVariable("curso") Curso curso) throws ExcepcionServicio {
        return moduloService.findByCurso(curso);
    }


    @GetMapping("cargar-notas")
    public String cargarNotasGet(Model model, String modulo) throws ExcepcionServicio {
        model.addAttribute("notasCargar", notaService.encontrarPorModulo(modulo));
        model.addAttribute("requestFilterNota", new RequestFilterNota());

        return "admin";
    }

    @PostMapping("cargar-notas")
    public String cargarNotas(@RequestBody ArrayList<RequestNotaJson> notas,
                              Model model) throws ExcepcionServicio {
        try {
            Modulo modulo = notaService.cargarPlanilla(notas);
            model.addAttribute("notasCargar", notaService.encontrarPorModulo(modulo.getId()));
            model.addAttribute("mensaje", "notas cargadas exitosamente");
            model.addAttribute("requestFilterNota", new RequestFilterNota());

        } catch (ExcepcionServicio excepcionServicio) {
            model.addAttribute("mensaje", excepcionServicio.getMessage());
            model.addAttribute("notasCargar",
                    notaService.encontrarPorModulo(notaService.readNota(notas.get(0).getId())
                            .orElseThrow(
                                    () -> new IllegalStateException("nota no encontrada")
                            ).getModulo().getId()));
            throw new IllegalStateException(excepcionServicio.getMessage());

        }

        return "admin";
    }

    @GetMapping("filter")
    public String filterss(Model model,
                           @RequestParam String filter) {
        model.addAttribute("notasCargar",
                notaRepository.filtersss("%" + filter + "%"));
        model.addAttribute("requestFilterNota", new RequestFilterNota());


        return "admin";
    }

    @PostMapping("filtrar")
    public String filtrar(Model model,
                          RequestFilterNota requestFilterNota) {

        try {
            model.addAttribute("notasCargar", notaService
                    .filtrar(requestFilterNota.estado,
                            requestFilterNota.alta,
                            requestFilterNota.turno,
                            requestFilterNota.notas
                    ));
        } catch (ExcepcionServicio excepcionServicio) {
            model.addAttribute("notasCargar", List.of());
            model.addAttribute("mensaje", excepcionServicio.getMessage());
        }

        return "admin";
    }

    @GetMapping("delete-nota/{notaId}")
    public String deleteNota(@PathVariable("notaId") String notaId,
                             Model model) throws ExcepcionControlador, ExcepcionServicio {
        Nota nota = notaService.readNota(notaId)
                .orElseThrow(
                        () -> new ExcepcionControlador("nota no encontrada")
                );

        notaService.deleteNota(nota.getId());

        model.addAttribute("notasCargar",
                notaService.encontrarPorModulo(nota.getModulo().getId()));

        model.addAttribute("requestFilterNota", new RequestFilterNota());

        return "redirect:/nota/filter?filter=" + nota.getCurso().getNombre();
    }

}

