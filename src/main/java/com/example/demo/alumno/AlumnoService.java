package com.example.demo.alumno;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoRepository;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.Modulo;
import com.example.demo.modulo.ModuloRepository;
import com.example.demo.nota.Nota;
import com.example.demo.nota.NotaRepository;
import com.example.demo.nota.NotaService;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo.enums.EstadoNota.*;
import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final ModuloRepository moduloRepository;
    private final NotaService notaService;
    private final NotaRepository notaRepository;
    private final UserService userService;

    public AlumnoApp createAlumno(AlumnoApp alumno) throws ExcepcionServicio {
        validar(alumno);
        alumno.setAlta(true);
        alumno.setFechaAlta(now());

        return alumnoRepository.save(alumno);
    }

    public Optional<AlumnoApp> readAlumno(String alumnoId) {
        return alumnoRepository.findById(alumnoId);
    }

    @Transactional
    public AlumnoApp updateAlumno(String alumnoId, AlumnoApp alumno) {

        AlumnoApp antiguoAlumno = alumnoRepository
                .findById(alumnoId)
                .orElseThrow(
                        () -> new IllegalStateException("alumno no encontrado")
                );

        if (alumno.getNombre() != null && alumno.getNombre().length() > 0 && !Objects.equals(alumno.getNombre(), antiguoAlumno.getNombre())) {
            antiguoAlumno.setNombre(alumno.getNombre());
        }
        if (alumno.getApellido() != null && alumno.getApellido().length() > 0 && !Objects.equals(alumno.getApellido(), antiguoAlumno.getApellido())) {
            antiguoAlumno.setApellido(alumno.getApellido());
        }

        if (alumno.getNumeroDeTelefono() != null && alumno.getNumeroDeTelefono().length() > 0 && !Objects.equals(alumno.getNumeroDeTelefono(), antiguoAlumno.getNumeroDeTelefono())) {
            antiguoAlumno.setNumeroDeTelefono(alumno.getNumeroDeTelefono());
        }
        if (alumno.getFechaNacimiento() != null && !Objects.equals(alumno.getFechaNacimiento(), antiguoAlumno.getFechaNacimiento())) {
            antiguoAlumno.setFechaNacimiento(alumno.getFechaNacimiento());
        }
        if (alumno.getDomicilio() != null && alumno.getDomicilio().length() > 0 && !Objects.equals(alumno.getDomicilio(), antiguoAlumno.getDomicilio())) {
            antiguoAlumno.setDomicilio(alumno.getDomicilio());
        }

        return alumno;
    }

    public void deleteAlumno(String alumnoId) {
        alumnoRepository.deleteById(alumnoId);
    }

    @Transactional
    public AlumnoApp bajaAlumno(String alumnoId) {

        AlumnoApp alumno = alumnoRepository
                .findById(alumnoId)
                .orElseThrow(
                        () -> new IllegalStateException("alumno no encontrado")
                );

        alumno.setAlta(false);
        alumno.setFechaBaja(now());
        return alumno;
    }

    @Transactional
    public AlumnoApp altaAlumno(String alumnoId) {

        AlumnoApp alumno = alumnoRepository
                .findById(alumnoId)
                .orElseThrow(
                        () -> new IllegalStateException("alumno no encontrado")
                );

        alumno.setAlta(true);
        alumno.setFechaBaja(null);
        return alumno;
    }

    public List<AlumnoApp> getAlumnos() {
        return alumnoRepository.findAll();
    }

    public void validar(AlumnoApp alumno) throws ExcepcionServicio {
        if (alumno.getNombre() == null || alumno.getNombre().isEmpty()) {
            throw new ExcepcionServicio("el nombre del alumno es un campo obligatorio");
        }
        if (alumno.getApellido() == null || alumno.getApellido().isEmpty()) {
            throw new ExcepcionServicio("el apellido es un campo obligatorio");
        }

        if (alumno.getDomicilio() == null || alumno.getDomicilio().isEmpty()) {
            throw new ExcepcionServicio("el domicilio es un campo obligatorio");
        }
        if (alumno.getNumeroDeTelefono() == null || alumno.getNumeroDeTelefono().isEmpty()) {
            throw new ExcepcionServicio("el numero de telefono es un campo obligatorio");
        }


    }

    @Transactional
    public AppUser setCurso(String id, String cursoId) throws ExcepcionServicio {

        System.out.println(id);



        AppUser user = userService.getOne(id)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuarion no encontrado")
                );

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("curso no encontrado")
                );

        user.getAlumno().getCursos().add(curso);

        List<Modulo> modulos = moduloRepository.findAllByCurso(curso);
        modulos.stream().forEach(System.out::println);

        modulos.stream()
                .forEach(
                (modulo -> {
                    Nota nota = notaService.createNota(
                            new Nota(modulo, user, curso, PENDIENTE, 0.0, null, null, null, null));
                    user.getAlumno().getNotas().add(nota);
                })
        );

        return user;
    }

    public List<Curso> findCursos(String alumnoId){
        return alumnoRepository.findCursos(alumnoId);
    }

    public List<Nota> findNotasByCurso(String cursoId, String alumnoId) throws ExcepcionServicio{

        Curso curso = cursoRepository
                .findById(cursoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("curso no encontrado")
                );
        AlumnoApp alumno = alumnoRepository
                .findById(alumnoId)
                .orElseThrow(
                        () -> new ExcepcionServicio("elumno no encontrado")
                );

        return notaRepository.encontrarNotasAlumno(curso, alumno);
    }

}
