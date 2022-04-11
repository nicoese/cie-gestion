package com.example.demo.nota;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.curso.Curso;
import com.example.demo.modulo.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, String> {

    @Query("SELECT a FROM Nota a WHERE a.modulo.curso = :curso AND a.user.alumno = :alumno")
    List<Nota> encontrarNotasAlumno(@Param("curso")Curso curso, @Param("alumno")AlumnoApp alumno);

    List<Nota> findByModulo(Modulo modulo);

    @Query("select a from Nota a where a.modulo = :modulo order by :attr asc ")
    List<Nota> filtrados(@Param("modulo")Modulo modulo, @Param("attr")String attr);

    @Query("select a from Nota a order by a.modulo.nombre")
    List<Nota> findtodo();

}
