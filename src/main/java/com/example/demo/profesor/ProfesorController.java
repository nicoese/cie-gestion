package com.example.demo.profesor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("profe")
public class ProfesorController {

    private ProfesorService profesorService;

    @PostMapping
    public ProfesorApp crearNuevoProfesor(@RequestBody ProfesorApp profe){
        ///TODO: validar
        return profesorService.createProfesor(profe);
    }

    @GetMapping("/{profeId}")
    public Optional<ProfesorApp> getProfe(@PathVariable("profeId")String profeId){
        ///TODO: validar
        return profesorService.readProfesor(profeId);
    }

    @PutMapping("/{profeId}")
    public ProfesorApp modificarProfe(@RequestBody ProfesorApp profe, @PathVariable("profeId")String profeId){
        ///TODO: validar
        return profesorService.updateProfesor(profe, profeId);
    }

    @DeleteMapping("{profeId}")
    public void eliminarProfe(@PathVariable("profeId")String profeId){
        profesorService.deleteProfesor(profeId);
    }

    @PostMapping("{profeId}")
    public ProfesorApp bajaProfe(@PathVariable("profeId")String profeId){
        return profesorService.bajaProfesor(profeId);
    }
}

