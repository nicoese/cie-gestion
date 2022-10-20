package com.example.demo.user;

import com.example.demo.excepciones.ExcepcionControlador;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static com.example.demo.enums.UserRole.*;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class MainController {

    private final UserService userService;


    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("login")
    public String login(){
        return "loginCie";
    }

    @GetMapping("logout")
    public String logout(){
        userService.getSession().invalidate();
        return "loginCie";
    }

    @GetMapping("buenlogin")
    public String successLogin(Model model) throws ExcepcionControlador {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();

        AppUser sessionUser = (AppUser) session.getAttribute("usersession");

        if (sessionUser.getRole() == ALUMNO) {
            model.addAttribute("alumno", sessionUser.getAlumno());

            return "redirect:/alumno/cursosAlumno/" + sessionUser.getAlumno().getId();
        }
        if (sessionUser.getRole() == ADMIN){
//            model.addAttribute("admin", sessionUser.getAdmin());

//            return "redirect:/admin/panel/" + sessionUser.getAdmin().getId();
            return "admin";
        }else {
            return "index";
        }
    }

    @GetMapping("menu-creacion")
    public String getMenuCreacion(Model model){
        model.addAttribute("menucreacion", "a");
        return "admin";
    }
    @GetMapping("menu-modificacion")
    public String getMenuModificacion(Model model){
        model.addAttribute("menumodificacion", "a");
        return "admin";
    }
    @GetMapping("notas")
    public String notas(Model model){
        model.addAttribute("menunotas", "a");
        return "admin";
    }

}
