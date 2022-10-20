package com.example.demo.inscripcion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, String> {

    @Query("select a from Inscripcion a order by a.fechaInscripcion desc")
    List<Inscripcion> findAllOrderDesc();

    List<Inscripcion> findByDNIAlumno(String DNIAlumno);
    List<Inscripcion> findByNombreCurso(String nombre);

    @Query("select a from Inscripcion a where a.nombreAlumno like :attr " +
            "or a.DNIAlumno like :attr " +
            "or a.nombreCurso like :attr " +
            "order by a.fechaInscripcion desc")
    List<Inscripcion> buscar(@Param("attr") String attr);
}
