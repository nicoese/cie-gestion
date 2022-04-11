package com.example.demo.nota;

import com.example.demo.excepciones.ExcepcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("nota")
@AllArgsConstructor
public class NotaController {

    private NotaService notaService;

    @GetMapping
    public List<Nota> buscarTodasLasNotas(){
        return notaService.getNotas();
    }

    @GetMapping("{notaId}")
    public Optional<Nota> buscarNota(@PathVariable("notaId")String notaId){
        return notaService.readNota(notaId);
    }

    @PostMapping
    public Nota crearNuevaNota(@RequestBody Nota nota){
        return notaService.createNota(nota);
    }

//    @PutMapping("{notaId}")
//    public Nota modificarNota(@RequestBody Nota nota,
//                              @PathVariable("notaId")String notaId) throws ExcepcionServicio {
//        return notaService.updateNota(nota, notaId);
//    }

    @DeleteMapping
    public void eliminarNota(String notaId) throws ExcepcionServicio {
        notaService.deleteNota(notaId);
    }
}
