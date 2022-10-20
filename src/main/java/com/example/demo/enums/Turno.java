package com.example.demo.enums;

public enum Turno {
    TURNO_MANANA, TURNO_TARDE, TURNO_VERSPERTINO;

    public Turno[] getTurnos(){
        return Turno.values();
    }
}
