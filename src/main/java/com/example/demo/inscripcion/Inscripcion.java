package com.example.demo.inscripcion;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.curso.Curso;
import com.example.demo.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private LocalDate fechaInscripcion;
    private String nombreCurso;
    private String nombreAdmin;
}
