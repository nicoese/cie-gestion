package com.example.demo.modulo;

import com.example.demo.creacion.CreacionService;
import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoRepository;
import com.example.demo.excepciones.ExcepcionServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class ModuloService {

    private ModuloRepository moduloRepository;
    private final CursoRepository cursoRepository;
    private final CreacionService creacionService;

    public Modulo createModulo(Modulo modulo){
        modulo.setAlta(true);
        modulo.setFechaCreacion(now());
        creacionService.infoNuevoModulo(modulo.getNombre(), modulo.getCurso().getNombre());
        return moduloRepository.save(modulo);
    }

    public List<Modulo> findAll(){
        return moduloRepository.findAll();
    }

    public Optional<Modulo> readModulo(String moduloId){
        return moduloRepository.findById(moduloId);
    }

    @Transactional
    public Modulo updateModulo(Modulo modulo, String moduloId) throws ExcepcionServicio {

        if (modulo.getNombre() == null || (modulo.getNombre().length() > 0 && modulo.getNombre().length() < 4)) {
            throw new ExcepcionServicio("el campo no puede ser tan corto");
        }

        Modulo antiguoModulo = moduloRepository
                .findById(moduloId)
                .orElseThrow(
                        () -> new IllegalStateException("modulo no encontrado")
                );

        if (Objects.equals(modulo.getNombre(), antiguoModulo.getNombre())) {
            throw new ExcepcionServicio("ya existe un curso con el nombre " + modulo.getNombre());
        }

        if (!modulo.getNombre().equals("")) {
            antiguoModulo.setNombre(modulo.getNombre());
        }

        antiguoModulo.setCurso(modulo.getCurso());

        return antiguoModulo;
    }

    public void deleteModulo(String moduloId){
        moduloRepository.deleteById(moduloId);
    }

    @Transactional
    public Modulo bajaModulo(String moduloId){

        Modulo modulo = moduloRepository
                .findById(moduloId)
                .orElseThrow(
                        () -> new IllegalStateException("modulo no encontrado")
                );

        modulo.setAlta(false);
        return modulo;
    }

    public List<Modulo> findAllByCurso(String cursoId) throws ExcepcionServicio{

        Curso curso = cursoRepository
                .findById(cursoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("curso no encontrado!")
                );

        return moduloRepository.findAllByCurso(curso);
    }

    public List<Modulo> findByCurso(Curso curso){
        return moduloRepository.findAllByCurso(curso);
    }
}
