package com.example.demo.creacion;

import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class CreacionService {
    private CreacionRepository creacionRepository;
    private UserService userService;

    public Creacion infoNuevoAlumno(String dniAlumno, String nombreAlumno){
        Creacion creacion = new Creacion();
        creacion.setDescripcion("generacion de un nuevo alumno");
        creacion.setAdminEncargado(this.getNombreAdmin());
        creacion.setDniAlumno(dniAlumno);
        creacion.setNombreAlumno(nombreAlumno);
        creacion.setFechaCreacion(now());
        creacionRepository.save(creacion);
        return creacion;
    }

    public Creacion infoNuevoCurso(String nombreCurso, String nombreCategoria){
        Creacion creacion = new Creacion();
        creacion.setDescripcion("generacion de curso");
        creacion.setAdminEncargado(this.getNombreAdmin());
        creacion.setNombreCurso(nombreCurso);
        creacion.setCategoriaCurso(nombreCategoria);
        creacion.setFechaCreacion(now());
        creacionRepository.save(creacion);
        return creacion;
    }

    public Creacion infoNuevoModulo(String nombreModulo, String nombreCurso){
        Creacion creacion = new Creacion();
        creacion.setDescripcion("generacion modulo");
        creacion.setAdminEncargado(this.getNombreAdmin());
        creacion.setNombreModulo(nombreModulo);
        creacion.setNombreCurso(nombreCurso);
        creacion.setFechaCreacion(now());
        creacionRepository.save(creacion);
        return creacion;
    }

    private String getNombreAdmin(){
        return ((AppUser) userService
                .getSession()
                .getAttribute("usersession"))
                .getAdmin()
                .getNombreCompleto();
    }
}
