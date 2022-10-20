package com.example.demo.modulo;

import com.example.demo.curso.Curso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RequestAlumnoIdAndCurso {
    private String alumnoId;
    private String cursoId;
}
