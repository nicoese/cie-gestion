package com.example.demo.nota;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.comision.Comision;
import com.example.demo.curso.Curso;
import com.example.demo.enums.EstadoNota;
import com.example.demo.enums.Turno;
import com.example.demo.modulo.Modulo;
import com.example.demo.user.AppUser;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.EnumType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Nota {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;

    @OneToOne
    private Modulo modulo;
    @ManyToOne
    private AppUser user;
    @OneToOne
    private Curso curso;

    @Column(length = 32, columnDefinition = "varchar(32) default 'PENDIENTE'")
    @Enumerated(STRING)
    private EstadoNota aprobado;

    @Column(length = 32, columnDefinition = "varchar(32)")
    @Enumerated(STRING)
    private Turno turno;

    @ManyToOne
    private Comision comision;

    private Double nota;
    private LocalDate fechaCreacion;
    private LocalDate fechaSetNota;


    public Nota(Modulo modulo,
                AppUser user,
                Curso curso,
                EstadoNota aprobado,
                Double nota,
                LocalDate fecha,
                LocalDate fechaSetNota,
                Turno turno,
                Comision comision) {
        this.modulo = modulo;
        this.user = user;
        this.curso = curso;
        this.aprobado = aprobado;
        this.nota = nota;
        this.fechaCreacion = fecha;
        this.fechaSetNota = fechaSetNota;
        this.turno = turno;
        this.comision = comision;
    }

    public EstadoNota[] getEstados(){
        return EstadoNota.values();
    }

}
