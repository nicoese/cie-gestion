package com.example.demo.inscripcion;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.curso.Curso;
import com.example.demo.enums.Turno;
import com.example.demo.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Inscripcion {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String nombreAlumno;
    private String DNIAlumno;
    private LocalDate fechaInscripcion;
    private String nombreCurso;
    private String nombreAdmin;
    private String nombreComision;

    @Enumerated(EnumType.STRING)
    private Turno turno;

    public Turno[] getTurnos(){
        return Turno.values();
    }
}
