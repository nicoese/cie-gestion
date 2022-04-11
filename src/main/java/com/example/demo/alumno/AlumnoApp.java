package com.example.demo.alumno;

import com.example.demo.curso.Curso;
import com.example.demo.enums.Sexo;
import com.example.demo.enums.UserRole;
import com.example.demo.nota.Nota;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static java.time.LocalDate.*;
import static javax.persistence.FetchType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AlumnoApp {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String apellido;
    private Boolean alta;
    private String numeroDeTelefono;
    private LocalDate fechaNacimiento;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private String domicilio;
    @Transient
    private Integer edad;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @ManyToMany(fetch = EAGER)
    private List<Curso> cursos;

    @OneToMany
    private List<Nota> notas;

    public AlumnoApp(String nombre,
                     String apellido,
                     String numeroDeTelefono,
                     LocalDate fechaNacimiento,
                     String domicilio,
                     Sexo sexo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDeTelefono = numeroDeTelefono;
        this.fechaNacimiento = fechaNacimiento;
        this.domicilio = domicilio;
        this.sexo = sexo;
    }

    public Integer obtenerEdad(){
        return Period.between(this.fechaNacimiento, now()).getYears();
    }

    public String getNombreCompleto(){
        return this.nombre + " " + this.apellido;
    }

    public Sexo[] getSexos(){
        return Sexo.values();
    }


}
