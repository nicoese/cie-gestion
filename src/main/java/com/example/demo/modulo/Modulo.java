package com.example.demo.modulo;

import com.example.demo.curso.Curso;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Comparator;


@Entity
@NoArgsConstructor
@Getter @Setter
public class Modulo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    @ManyToOne
    private Curso curso;

    private String nombre;
    private Boolean alta;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private LocalDate fechaCreacion;


}
