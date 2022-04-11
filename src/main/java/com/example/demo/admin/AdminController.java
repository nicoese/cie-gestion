package com.example.demo.admin;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.excepciones.ExcepcionControlador;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.Modulo;
import com.example.demo.modulo.ModuloService;
import com.example.demo.nota.Nota;
import com.example.demo.nota.NotaService;
import com.example.demo.nota.RequestNotaJson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CursoService cursoService;
    private final ModuloService moduloService;
    private final NotaService notaService;

    @PostMapping
    public AdminApp crearNuevoAdmin(@RequestBody AdminApp admin) {
        ///TODO: validaciones
        return adminService.createAdmin(admin);
    }

    @GetMapping("/{adminId}")
    public Optional<AdminApp> getAdmin(@PathVariable String adminId) {
        ///TODO: validar
        return adminService.readAdmin(adminId);
    }

    @PutMapping("/{adminId}")
    public AdminApp modificarAdmin(@RequestBody AdminApp admin, @PathVariable("adminId") String adminId) {
        ///TODO: validar
        return adminService.updateAdmin(admin, adminId);
    }

    @DeleteMapping("/{adminId}")
    public void eliminarAdmin(@PathVariable("adminId") String adminId) {
        adminService.deleteAdmin(adminId);
    }

    @PostMapping("/{adminId}")
    public AdminApp bajaAdmin(@PathVariable("adminId") String adminId) {
        return adminService.bajaAdmin(adminId);
    }

    @GetMapping("/desplegarcursos/{url}")
    public String desplegarCursos(Model model,
                                  @PathVariable("url") String url) {

        List<Curso> cursos = cursoService.getAll();
        model.addAttribute("cursos", cursos);
        model.addAttribute("url", url);
        return "admin";
    }

    @GetMapping("/desplegarmodulos")
    public String desplegarModulos(String cursoId,
                                   String url,
                                   Model model) throws ExcepcionServicio {
        List<Modulo> modulos = moduloService
                .findAllByCurso(cursoId);
        model.addAttribute("modulos", modulos);
        model.addAttribute("url", url);
        return "admin";
    }

    @GetMapping("/desplegarnotas")
    public String desplegarNotas(
            String moduloId,
            Model model) throws ExcepcionServicio {
        List<Nota> notas = notaService
                .encontrarPorModulo(moduloId);
        model.addAttribute("notas", notas);
        return "admin";
    }

    @GetMapping("/modificarnotas/{notaId}")
    public String modificarNota(@PathVariable("notaId") String notaId,
                                Model model) throws ExcepcionControlador {
        model.addAttribute("notaAeditar", notaService.readNota(notaId)
                .orElseThrow(
                        () -> new ExcepcionControlador("no se ha encontrado la nota")
                ));
        return "admin";
    }

    @PostMapping("/modificarnotas")
    public String modificarNotas(Modulo modulo,
                                 String notaId,
                                 Double nota,
                                 Model model) throws ExcepcionServicio {
        Nota notaX = notaService.updateNota(nota, notaId);
        System.out.println(notaX);
        model.addAttribute("notas",
                notaService.encontrarPorModulo(modulo.getId()));

        return "admin";
    }

    @GetMapping("delete-nota/{notaId}")
    public String deleteNota(@PathVariable("notaId") String notaId,
                             Model model) throws ExcepcionControlador, ExcepcionServicio {
        Nota nota = notaService.readNota(notaId)
                .orElseThrow(
                        () -> new ExcepcionControlador("nota no encontrada")
                );

        System.out.println(nota.toString());

        notaService.deleteNota(notaId);

        model.addAttribute("notas",
                notaService.encontrarPorModulo(nota.getModulo().getId()));
        return "admin";
    }

    @GetMapping("crearcurso")
    public String crearCursoGet(Model model) {
        model.addAttribute("curso", new Curso());
        return "crearCurso";
    }

    @PostMapping("crearcurso")
    public String crearCurso(Curso curso,
                             Model model) throws ExcepcionControlador {
        this.validarString(curso.getNombre());
        cursoService.createCurso(curso);
        return "crearCurso";
    }

    @GetMapping("crearmodulo")
    public String crearModuloGet(Model model) {
        model.addAttribute("modulo", new Modulo());
        return "";
    }

    @PostMapping("crearmodulo")
    public String crearModulo(Modulo modulo,
                              Model model) throws ExcepcionControlador {
        this.validarString(modulo.getNombre());
        moduloService.createModulo(modulo);
        return "";
    }

    @GetMapping("crearnota")
    public String crearNotaGet(Model model) {
        model.addAttribute("nota", new Nota());
        return "";
    }

    @PostMapping("crearnota")
    public String crearNota(Nota nota,
                            Model model) throws ExcepcionControlador {
        if (nota.getNota() == null || nota.getNota() < 1 || nota.getNota() > 10) {
            throw new ExcepcionControlador("la calificacion debe estar entre 1 y 10");
        }
        notaService.createNota(nota);
        return "";
    }

    @GetMapping("crearadmin")
    public String crearAdminGet(Model model) {
        model.addAttribute("admin", new AdminApp());
        return "";
    }

    @PostMapping("crearadmin")
    public String crearAdmin(AdminApp adminApp,
                             Model model) throws ExcepcionControlador {
        this.validarString(adminApp.getNombre());
        this.validarString(adminApp.getApellido());
        this.validarString(adminApp.getDomicilio());
        this.validarString(adminApp.getNumeroDeTelefono());
        adminService.createAdmin(adminApp);
        return "";
    }

    private void validarString(String campo) throws ExcepcionControlador {
        if (campo == null || campo.length() <= 4) {
            throw new ExcepcionControlador("el campo no puede estar vacio o tener menos de 4 caracteres");
        }
    }

    @GetMapping("panel/{adminId}")
    public String panelAdmin(@PathVariable("adminId") String adminId,
                             Model model) throws ExcepcionControlador {
        model.addAttribute("admin", adminService.readAdmin(adminId).orElseThrow(
                () -> new ExcepcionControlador("admin no encontrado")
        ));
        return "admin";
    }

    @GetMapping("cargar-notas")
    public String cargarNotasGet(Model model, String moduloId) throws ExcepcionServicio {
        model.addAttribute("notasCargar", notaService.encontrarPorModulo(moduloId));
        return "admin";
    }

    @PostMapping("cargar-notas")
    public String cargarNotas(@RequestBody ArrayList<RequestNotaJson> notas,
                              Model model) throws ExcepcionServicio {
        Modulo modulo = notaService.cargarPlanilla(notas);
        model.addAttribute("notasCargar", notaService.encontrarPorModulo(modulo.getId()));
        return "admin";
    }

}
