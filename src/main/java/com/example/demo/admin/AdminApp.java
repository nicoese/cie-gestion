package com.example.demo.admin;

import com.example.demo.enums.Sexo;
import com.example.demo.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdminApp  {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private String dni;
    private String domicilio;
    private Sexo sexo;
    private String numeroDeTelefono;
    private LocalDate fechaNacimiento;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

//    @OneToOne
//    private AppUser usuario;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Boolean alta;


    public AdminApp(String nombre, String apellido, String email, String contrasena, String dni, String domicilio, Sexo sexo, String numeroDeTelefono, LocalDate fechaNacimiento, LocalDate fechaAlta, LocalDate fechaBaja, UserRole role, Boolean alta) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.dni = dni;
        this.domicilio = domicilio;
        this.sexo = sexo;
        this.numeroDeTelefono = numeroDeTelefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaAlta = fechaAlta;
        this.fechaBaja = fechaBaja;
        this.role = role;
        this.alta = alta;
    }

    public String getNombreCompleto(){
        return this.nombre +" "+ this.apellido;
    }

}
