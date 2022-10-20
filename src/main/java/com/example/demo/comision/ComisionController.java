package com.example.demo.comision;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("comision")
@AllArgsConstructor
public class ComisionController {

    private final ComisionService comisionService;

    @GetMapping("crear")
    public String crearComisionGet(Model model) {
        model.addAttribute("nuevaComision", new Comision());
        return "admin";
    }

    @PostMapping("crear")
    public String crearComision(Comision comision,
                                Model model) {
        try {
            comisionService.createComision(comision);
            model.addAttribute("nuevaComision", new Comision());
            model.addAttribute("mensaje", "Comision creada exitosamente!");
        } catch (IllegalStateException e) {
            model.addAttribute("nuevaComision", new Comision());
            model.addAttribute("error", e.getMessage());
        }
        return "admin";
    }

    @ResponseBody
    @GetMapping("find-by-name/{comisionName}")
    public List<Comision> getComisionesByName(
            @PathVariable("comisionName") String comisionName,
            Model model) {
        return comisionService.findAllByName(comisionName);
    }
}
