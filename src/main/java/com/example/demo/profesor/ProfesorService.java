package com.example.demo.profesor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Optional;

import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class ProfesorService {

    private ProfesorRepository profesorRepository;

    public ProfesorApp createProfesor(ProfesorApp profesor){
        profesor.setAlta(true);
        profesor.setFechaAlta(now());
        return profesorRepository.save(profesor);
    }

    public Optional<ProfesorApp> readProfesor(String profesorId){
        return profesorRepository.findById(profesorId);
    }

    @Transactional
    public ProfesorApp updateProfesor(ProfesorApp profesor, String profesorId){

        ProfesorApp antiguoProfesor = profesorRepository
                .findById(profesorId)
                .orElseThrow(
                        () -> new IllegalStateException("profesor no encontrado")
                );
        return profesor;
    }

    public void deleteProfesor(String profesorId){
        profesorRepository.deleteById(profesorId);
    }

    @Transactional
    public ProfesorApp bajaProfesor(String profesorId){
        ProfesorApp profesor = profesorRepository
                .findById(profesorId)
                .orElseThrow(
                        () -> new IllegalStateException("profesor no encontrado")
                );
        profesor.setFechaBaja(now());
        return profesor;
    }
}
