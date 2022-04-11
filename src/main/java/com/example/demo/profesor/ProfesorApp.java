package com.example.demo.profesor;

import com.example.demo.curso.Curso;
import com.example.demo.enums.CursoLista;
import com.example.demo.enums.Sexo;
import com.example.demo.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.EnumType.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class ProfesorApp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String nombre;
    private String apellido;
    private String contrasena;
    private String telefono;
    private String dni;
    private String domicilio;
    private Boolean alta;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private LocalDate fechaNacimiento;

    @Enumerated(STRING)
    private UserRole role;
    @Enumerated (STRING)
    private Sexo sexo;

    @OneToMany
    private List<Curso> cursos;

}
