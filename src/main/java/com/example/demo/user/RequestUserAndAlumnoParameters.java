package com.example.demo.user;

import com.example.demo.enums.Sexo;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class RequestUserAndAlumnoParameters {

    private String userEmail;
    private String userDni;
    private String nombre;
    private String apellido;
    private String numeroDeTelefono;
    private LocalDate fechaDeNacimiento;
    private String domicilio;
    private Sexo sexo;
}
