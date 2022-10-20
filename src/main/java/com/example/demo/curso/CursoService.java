package com.example.demo.curso;

import com.example.demo.creacion.CreacionService;
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
public class CursoService {

    private CursoRepository cursoRepository;
    private CreacionService creacionService;

    public Curso createCurso(Curso curso) {

        cursoRepository.findByNombre(curso.getNombre()).ifPresent(
                (curso1 -> {
                    throw new IllegalStateException("ya existe un curso con el nombre " + curso.getNombre());
                })
        );

        curso.setAlta(true);
        curso.setFechaAlta(now());
        creacionService.infoNuevoCurso(curso.getNombre(), curso.getCategoria().name());
        return cursoRepository.save(curso);
    }

    public Optional<Curso> readCurso(String cursoId) {
        return cursoRepository.findById(cursoId);
    }

    @Transactional
    public Curso updateCurso(Curso curso, String cursoId) throws ExcepcionServicio {


        if (curso.getNombre() == null || (curso.getNombre().length() > 0 && curso.getNombre().length() < 4)) {
            throw new ExcepcionServicio("el campo no puede ser tan corto");
        }

        Curso antiguoCurso = cursoRepository
                .findById(cursoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("curso no encontrado")
                );

        if (Objects.equals(curso.getNombre(), antiguoCurso.getNombre())) {
            throw new ExcepcionServicio("ya existe un curso con el nombre " + curso.getNombre());
        }

        if (!curso.getNombre().equals("")) {
            antiguoCurso.setNombre(curso.getNombre());
        }

        antiguoCurso.setCategoria(curso.getCategoria());

        return antiguoCurso;
    }

    public void deleteCurso(String cursoId) {
        cursoRepository.deleteById(cursoId);
    }

    @Transactional
    public Curso bajaCurso(String cursoId) {

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
