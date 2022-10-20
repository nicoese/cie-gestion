package com.example.demo.inscripcion;

import com.example.demo.comision.Comision;
import com.example.demo.comision.ComisionRepository;
import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.enums.Turno;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.ModuloService;
import com.example.demo.nota.Nota;
import com.example.demo.nota.NotaService;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

@Service
@AllArgsConstructor
public class InscripcionService {

    private InscripcionRepository inscripcionRepository;
    private UserService userService;
    private ModuloService moduloService;
    private NotaService notaService;
    private final CursoService cursoService;
    private final ComisionRepository comisionRepository;

    public List<Inscripcion> getAllInscripciones() {
        return inscripcionRepository.findAllOrderDesc();
    }

    public Inscripcion createInscripcion(String nombreAlumno,
                                         String DNIAlumno,
                                         String nombreCurso,
                                         Turno turno,
                                         String nombreComision) {
        Inscripcion inscripcion = new Inscripcion();
        AppUser admin = (AppUser) userService.getSession().getAttribute("usersession");
        inscripcion.setNombreAdmin(admin.getAdmin().getNombreCompleto());
        inscripcion.setFechaInscripcion(now());
        inscripcion.setNombreAlumno(nombreAlumno);
        inscripcion.setDNIAlumno(DNIAlumno);
        inscripcion.setNombreCurso(nombreCurso);
        inscripcion.setTurno(turno);
        inscripcion.setNombreComision(nombreComision);
        return inscripcionRepository.save(inscripcion);
    }

    public Optional<Inscripcion> readInscripcion(String inscripcionId) {
        return inscripcionRepository.findById(inscripcionId);
    }

    @Transactional
    public Inscripcion updateInscripcion(Inscripcion inscripcion, String inscripcionId) {

        Inscripcion antiguaInscripcion = inscripcionRepository
                .findById(inscripcionId)
                .orElseThrow(
                        () -> new IllegalStateException("datos no encontrados")
                );
        return antiguaInscripcion;

    }

    public void deleteInscripcion(String inscripcionId) {
        inscripcionRepository.deleteById(inscripcionId);
    }

    @Transactional
    public Inscripcion enlistar(String userId,
                                String cursoId,
                                Turno turno,
                                Comision comision) throws ExcepcionServicio {
        AppUser userAlumno = userService.findByUsername(userId)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );

        Curso curso = cursoService.readCurso(cursoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("curso no encontrado")
                );

        Comision updateComision = comisionRepository
                .findById(comision.getId())
                .orElseThrow(
                        () -> new IllegalStateException("Comision con id "+comision.getId()+" no ha sido encontrada")
                );

        userAlumno.getAlumno().getCursos().add(curso);
        updateComision.getAlumnos().add(userAlumno);

        moduloService.findAllByCurso(curso.getId())
                .forEach(
                        modulo -> {
                            Nota nota = new Nota();
                            nota.setModulo(modulo);
                            nota.setCurso(curso);
                            nota.setUser(userAlumno);
                            nota.setTurno(turno);
                            nota.setComision(updateComision);
                            notaService.createNota(nota);
                            userAlumno.getAlumno().getNotas().add(nota);
                        }
                );


        return this.createInscripcion(
                userAlumno.getAlumno().getNombreCompleto(),
                userAlumno.getUsername(),
                curso.getNombre(),
                turno,
                updateComision.getNombre());
    }

    public List<Inscripcion> buscarInscripciones(String cadena) {
        cadena = "%" + cadena + "%";
        return inscripcionRepository.buscar(cadena);
    }

    public List<Inscripcion> filtrarInscripciones(String nombreAdmin,
                                                  Turno turno,
                                                  List<Inscripcion> inscripciones) {
        if (!nombreAdmin.equals("")) {
            inscripciones = inscripciones.stream().filter(inscripcion -> {
                return inscripcion.getNombreAdmin().equals(nombreAdmin);
            }).collect(Collectors.toList());
        }
        if (turno != null) {
            inscripciones = inscripciones.stream().filter(inscripcion -> {
                return inscripcion.getTurno().equals(turno);
            }).collect(Collectors.toList());
        }
        if (inscripciones.size()==0){
            throw new IllegalStateException("No se encontraron registros");
        }
        return inscripciones;
    }

}
