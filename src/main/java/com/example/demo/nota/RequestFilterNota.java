package com.example.demo.nota;

import com.example.demo.enums.EstadoNota;
import com.example.demo.enums.Turno;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class RequestFilterNota {
    EstadoNota estado;
    Boolean alta;
    Turno turno;
    ArrayList<Nota> notas;
}
