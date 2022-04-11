package com.example.demo.inscripcion;

import com.example.demo.curso.Curso;
import com.example.demo.curso.CursoService;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.modulo.ModuloService;
import com.example.demo.nota.Nota;
import com.example.demo.nota.NotaService;
import com.example.demo.user.AppUser;
import com.example.demo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
@AllArgsConstructor
public class InscripcionService {

    private InscripcionRepository inscripcionRepository;
    private UserService userService;
    private ModuloService moduloService;
    private NotaService notaService;
    private final CursoService cursoService;

    public Inscripcion createInscripcion(String nombreAlumno,
                                         String nombreCurso) {
        Inscripcion inscripcion = new Inscripcion();
        AppUser admin = (AppUser) userService.getSession().getAttribute("usersession");
        inscripcion.setNombreAdmin(admin.getAdmin().getNombreCompleto());
        inscripcion.setFechaInscripcion(now());
        inscripcion.setNombreAlumno(nombreAlumno);
        inscripcion.setNombreCurso(nombreCurso);
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
                                String cursoId) throws ExcepcionServicio {
        AppUser userAlumno = userService.findByUsername(userId)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );

        Curso curso = cursoService.readCurso(cursoId)
                        .orElseThrow(
                                () -> new ExcepcionServicio("curso no encontrado")
                        );

        userAlumno.getAlumno().getCursos().add(curso);

        moduloService.findAllByCurso(curso.getId())
                .forEach(
                        modulo -> {
                            Nota nota = new Nota();
                            nota.setModulo(modulo);
                            nota.setCurso(curso);
                            nota.setUser(userAlumno);
                            notaService.createNota(nota);
                            userAlumno.getAlumno().getNotas().add(nota);
                        }
                );



        return this.createInscripcion(userAlumno.getAlumno().getNombreCompleto(),
                curso.getNombre());
    }


}
