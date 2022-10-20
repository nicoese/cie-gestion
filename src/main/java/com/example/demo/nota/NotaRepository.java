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

    @Query("select a from Nota a where a.modulo = :modulo order by :attr desc ")
    List<Nota> filtrados(@Param("modulo")Modulo modulo, @Param("attr")String attr);

    @Query("select n from Nota n where n.curso.nombre like :columna " +
            "or n.modulo.nombre like :columna " +
            "or n.user.alumno.apellido like :columna " +
            "or n.comision.nombre like :columna " +
            "or n.user.username like :columna order by n.modulo.nombre asc")
    List<Nota> filtersss(@Param("columna")String columna);

    @Query("select n from Nota n where n.aprobado = :aprobado")
    List<Nota> estado(@Param("aprobado")String aprobado);

    @Query("select a from Nota a order by a.modulo.nombre")
    List<Nota> findtodo();

}
