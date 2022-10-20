package com.example.demo.admin;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.enums.Sexo;
import com.example.demo.excepciones.ExcepcionControlador;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.Modulo;
import com.example.demo.modulo.ModuloService;
import com.example.demo.nota.Nota;
import com.example.demo.nota.NotaService;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CursoService cursoService;
    private final ModuloService moduloService;
    private final NotaService notaService;
    private final UserService userService;

    @GetMapping
    public String admin() {
        return "admin";
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

//    @PostMapping("/{adminId}")
//    public AdminApp bajaAdmin(@PathVariable("adminId") String adminId) {
//        return adminService.bajaAdmin(adminId);
//    }

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


//    @GetMapping("crearnota")
//    public String crearNotaGet(Model model) {
//        model.addAttribute("nota", new Nota());
//        return "";
//    }

    @PostMapping("crearnota")
    public String crearNota(Nota nota,
                            Model model) throws ExcepcionControlador {
        if (nota.getNota() == null || nota.getNota() < 1 || nota.getNota() > 10) {
            throw new ExcepcionControlador("la calificacion debe estar entre 1 y 10");
        }
        notaService.createNota(nota);
        return "";
    }

    @GetMapping("nuevo-admin")
    public String crearAdminGet(Model model) {
        model.addAttribute("newAdmin", new AppUser());
        return "admin";
    }

    @PostMapping("crearadmin")
    public String crearAdmin(String nombre,
                             String apellido,
                             String username,
                             String email,
                             String password,
                             String passwordCheck,
                             Sexo sexo,
                             Model model) throws ExcepcionControlador {

        try {
            if (nombre.equals(null)
                    || apellido.equals("")
                    || username.equals("")
                    || email.equals("")
                    || password.equals("")
                    || passwordCheck.equals("")) throw new IllegalStateException("Los campos obligatorios no se pueden enviar vacios");

            if (!password.equals(passwordCheck))
                throw new IllegalStateException("Las contrasenas deben coincidir");

            AdminApp admin = adminService.createAdmin(nombre, apellido, sexo);
            AppUser user = userService.addNewUserAdmin(email, username, password);
            adminService.setAdminToUser(user, admin);


        } catch (IllegalStateException e) {
            model.addAttribute("newAdmin", new AppUser());
            model.addAttribute("error", e.getMessage());
        }

        return "admin";
    }

    @GetMapping("panel/{adminId}")
    public String panelAdmin(@PathVariable("adminId") String adminId,
                             Model model) throws ExcepcionControlador {
        model.addAttribute("admin", adminService.readAdmin(adminId).orElseThrow(
                () -> new ExcepcionControlador("admin no encontrado")
        ));
        return "admin";
    }


}
