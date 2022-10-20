package com.example.demo.creacion;

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
@Getter @Setter
public class Creacion {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    private String descripcion;
    private String adminEncargado;
    private String nombreAlumno;
    private String dniAlumno;
    private String nombreCurso;
    private String categoriaCurso;
    private String nombreModulo;
    private LocalDate fechaCreacion;
}
