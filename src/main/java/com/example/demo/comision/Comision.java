package com.example.demo.comision;

import com.example.demo.curso.Curso;
import com.example.demo.enums.Turno;
import com.example.demo.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@NoArgsConstructor
@Getter @Setter
@AllArgsConstructor
public class Comision {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String ano;

    @Enumerated(STRING)
    private Turno turno;

    @OneToMany
    private List<AppUser> alumnos;

    @ManyToOne
    private Curso curso;
}
