package com.example.demo.modulo;

import com.example.demo.curso.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuloRepository extends JpaRepository<Modulo, String> {

    List<Modulo> findAllByCurso(Curso curso);
    Optional<Modulo> findByNombre(String nombre);
}
