package com.example.demo.comision;

import com.example.demo.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ComisionService {

    private final ComisionRepository comisionRepository;

    public void createComision(Comision comision){
        if (comision.getNombre()==null || comision.getNombre().length()<6)
            throw new IllegalStateException("El nombre no debe ser menor a 6 caracteres");
        if (comision.getAno()==null)
            throw new IllegalStateException("Seleccione un ano para la comision");
        if (comision.getTurno()==null)
            throw new IllegalStateException("Seleccione un turno para la comision");
        if (comision.getCurso()==null)
            throw new IllegalStateException("Seleccione un curso para la comision");
        comision.setAlumnos(new ArrayList<AppUser>());
        comisionRepository.save(comision);
    }
    
    public List<Comision> findAllByName(String name){
        List<Comision> byNombreLikeOrderByNombreAsc = comisionRepository.findByNombreContainingOrderByNombreAsc(name);
        return byNombreLikeOrderByNombreAsc;
    }
}
