package com.example.demo.comision;

import com.example.demo.curso.Curso;
import com.example.demo.enums.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComisionRepository extends JpaRepository<Comision, String> {

    List<Comision> findByNombreContainingOrderByNombreAsc(String nombre);
    List<Comision> findAllByAnoOrderByAnoDesc(String ano);
    List<Comision> findAllByTurnoOrderByNombreAsc(Turno turno);
    List<Comision> findAllByCursoOrderByNombreAsc(Curso curso);
}
