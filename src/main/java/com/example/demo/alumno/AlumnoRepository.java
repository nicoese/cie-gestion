package com.example.demo.alumno;

import com.example.demo.curso.Curso;
import com.example.demo.nota.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<AlumnoApp, String> {


    @Query("select a.cursos from AlumnoApp a where a.id = :alumnoId")
    List<Curso> findCursos(@Param("alumnoId") String alumnoId);



//    @Query("select n from AlumnoApp n where n.id = :id and n.notas")
//    List<Nota> findNotasByCurso()

}
