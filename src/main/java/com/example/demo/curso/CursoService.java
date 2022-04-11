package com.example.demo.curso;

import com.example.demo.creacion.CreacionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class CursoService {

    private CursoRepository cursoRepository;
    private CreacionService creacionService;

    public Curso createCurso(Curso curso){
        curso.setAlta(true);
        curso.setFechaAlta(now());
        creacionService.infoNuevoCurso(curso.getNombre(), curso.getCategoria().name());
        return cursoRepository.save(curso);
    }

    public Optional<Curso> readCurso(String cursoId){
        return cursoRepository.findById(cursoId);
    }

    @Transactional
    public Curso updateCurso(Curso curso, String cursoId){

        Curso antiguoCurso = cursoRepository
                .findById(cursoId)
                .orElseThrow(
                        () -> new IllegalStateException("curso no encontrado")
                );

        return antiguoCurso;
    }

    public void deleteCurso(String cursoId){
        cursoRepository.deleteById(cursoId);
    }

    @Transactional
    public Curso bajaCurso(String cursoId){

        Curso cursoABorrar = cursoRepository
                .findById(cursoId)
                .orElseThrow(
                        () -> new IllegalStateException("curso no encontrado")
                );

        cursoABorrar.setAlta(false);
        return cursoABorrar;
    }

    public List<Curso> getAll() {
        return cursoRepository.findAll();
    }
}
