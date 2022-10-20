package com.example.demo.curso;

import com.example.demo.enums.Categoria;
import com.example.demo.enums.CursoLista;
import com.example.demo.enums.Duracion;
import com.example.demo.modulo.Modulo;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.EnumType.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Curso implements Comparable<Curso>{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String nombre;
    private Boolean alta;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    @Enumerated(STRING)
    private Categoria categoria;

    @Enumerated(STRING)
    private Duracion duracion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curso curso = (Curso) o;
        return Objects.equals(nombre, curso.nombre);
    }


    @Override
    public int compareTo(Curso o) {
        return this.nombre.compareTo(o.nombre);
    }


    public String getNombre() {
        return nombre;
    }

    public Curso(String nombre, Boolean alta, LocalDate fechaAlta, LocalDate fechaBaja) {
        this.nombre = nombre;
        this.alta = alta;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
    }

    public Categoria[] getCategorias(){
        return Categoria.values();
    }
}
