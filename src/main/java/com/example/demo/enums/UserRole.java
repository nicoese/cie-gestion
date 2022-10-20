package com.example.demo.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole {
    PROFESOR,
    ALUMNO,
    ADMIN;

    public List<SimpleGrantedAuthority> getGrantedAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}
