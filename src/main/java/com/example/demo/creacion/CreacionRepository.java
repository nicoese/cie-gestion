package com.example.demo.creacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreacionRepository extends JpaRepository<Creacion, String> {

    Optional<Creacion> findByDniAlumno(String dni);
    Optional<Creacion> findByNombreCurso(String nombreCurso);
    List<Creacion> findAllByNombreModulo(String nombreModulo);
}
