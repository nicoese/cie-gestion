package com.example.demo.nota;

import com.example.demo.alumno.AlumnoApp;
import com.example.demo.alumno.AlumnoRepository;
import com.example.demo.alumno.AlumnoService;
import com.example.demo.enums.EstadoNota;
import com.example.demo.enums.Turno;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.Modulo;
import com.example.demo.modulo.ModuloService;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.enums.EstadoNota.*;
import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;
    private final AlumnoRepository alumnoRepository;
    private final ModuloService moduloService;
    private final UserService userService;

    public List<Nota> getNotas() {
        return notaRepository.findAll();
    }

    public Optional<Nota> readNota(String notaId) {
        return notaRepository.findById(notaId);
    }

    public Nota createNota(Nota nota) {
        nota.setFechaCreacion(now());
        nota.setAprobado(PENDIENTE);
        nota.setNota(0.0);
        return notaRepository.save(nota);
    }

    @Transactional
    public Nota updateNota(Double nota, String notaId) {
        Nota notaAEditar = notaRepository
                .findById(notaId)
                .orElseThrow(
                        () -> new IllegalStateException("nota no encontrada")
                );

        if (nota != null) {
            if (nota > 10 || nota < 0) throw new IllegalStateException("la nota no puede ser mayor a 10 menor a 0");
            if (nota >= 7) notaAEditar.setAprobado(APROBADO);
            if (nota < 7) notaAEditar.setAprobado(DESAPROBADO);
            if (nota == 0) notaAEditar.setAprobado(PENDIENTE);
            notaAEditar.setFechaSetNota(now());
            notaAEditar.setNota(nota);
        }

        return notaAEditar;
    }

    @Transactional
    public void deleteNota(String notaId) throws ExcepcionServicio {

        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(
                        () -> new ExcepcionServicio("nota no encontrada")
                );

        AppUser user = userService.getOne(nota.getUser().getId())
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );
        user.getAlumno().getNotas().remove(nota);

        notaRepository.deleteById(notaId);
    }

    public List<Nota> encontrarPorModulo(String moduloId) throws ExcepcionServicio {
        Modulo modulo = moduloService
                .readModulo(moduloId)
                .orElseThrow(
                        () -> new ExcepcionServicio("modulo no encontrado")
                );
        return notaRepository.findByModulo(modulo);
    }

    @Transactional
    public Modulo cargarPlanilla(ArrayList<RequestNotaJson> notasJson) throws ExcepcionServicio {

        final var nota = new Nota[1];

        try {
            notasJson.forEach((notaJson) -> {
                nota[0] = this.updateNota(notaJson.getNota(),
                        notaJson.getId());
            });
            return nota[0].getModulo();
        } catch (IllegalStateException e) {
            throw new ExcepcionServicio(e.getMessage());
        }


    }

    public List<Nota> filtrar(EstadoNota estado,
                              Boolean alta,
                              Turno turno,
                              List<Nota> notas) throws ExcepcionServicio {

        if (notas==null)throw new ExcepcionServicio("No se encontraron registros");

        List<Nota> finalNotas = new ArrayList<Nota>();

        for (Nota nota :
                notas) {
            finalNotas.add(notaRepository.findById(nota.getId())
                    .orElseThrow(
                            () -> new IllegalStateException("nota no encontrada")
                    )
            );
        }

            if (estado != null) {
                finalNotas = finalNotas.stream()
                        .filter((nota) -> nota.getAprobado().equals(estado))
                        .collect(Collectors.toList());
            }
            if (alta != null) {
                finalNotas = finalNotas.stream()
                        .filter((nota) -> alta.equals(nota.getUser().getAlumno().getAlta()))
                        .collect(Collectors.toList());
            }
            if (turno!=null){
                finalNotas = finalNotas.stream()
                        .filter((nota -> nota.getTurno().equals(turno)))
                        .collect(Collectors.toList());
            }
        if(finalNotas.size()==0){
            throw new ExcepcionServicio("No se encontraron registros");
        }

        return finalNotas;
    }
}
