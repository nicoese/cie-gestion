package com.example.demo.user;

import com.example.demo.admin.AdminApp;
import com.example.demo.alumno.AlumnoApp;
import com.example.demo.curso.CursoRepository;
import com.example.demo.curso.CursoService;
import com.example.demo.enums.*;
import com.example.demo.excepciones.ExcepcionServicio;
import com.example.demo.security.PasswordConfig;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.example.demo.enums.UserRole.*;
import static java.time.LocalDate.now;
import static java.util.stream.IntStream.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CursoRepository cursoRepository;

    public List<AppUser> getAppUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> getOne(String id) {
        return userRepository.findById(id);
    }

    public AppUser addNewUserAdmin(String email, String username, String password){
        AppUser user = new AppUser();
        userRepository.findByEmail(email).ifPresent((user1) -> {
                            throw new IllegalStateException("El email " + email + " esta en uso");
                        });
        user.setEmail(email);
        userRepository.findByUsername(username).ifPresent(
                (user1) -> {
                    throw new IllegalStateException("El dni " + username + " ya esta registrado");
                }
        );
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    return user;
    }

    public AppUser addNewUser(AppUser user) throws ExcepcionServicio {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) throw new ExcepcionServicio("email en uso");
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ExcepcionServicio("el dni ya esta en uso actualmente");
        }

        this.validarEmail(user.getEmail());
        this.validarUsername(user.getUsername());

        user.setRole(ALUMNO);

        user.setPassword(null);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);

        return userRepository.save(user);
    }

    public Boolean validarEmail(String email) throws ExcepcionServicio {

        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }

        if (!result) {
            throw new ExcepcionServicio("email invalido");
        } else return true;
    }

    public void validarUsername(String username) throws ExcepcionServicio {

        if (username.length() < 7 || username.length() > 8) {
            throw new ExcepcionServicio("dni invalido");
        }

        try {
            Long.parseLong(username);
        } catch (NumberFormatException e) {
            throw new ExcepcionServicio("dni invalido");
        }

    }

    public String generatePW() {
        return PasswordConfig.generatePassayPassword();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public AppUser updateUser(String username, AppUser user) throws ExcepcionServicio {

        AppUser newUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ExcepcionServicio("usuario no encontrado"));

        if (!user.getEmail().equals(newUser.getEmail())) {
            userRepository.findByEmail(user.getEmail())
                    .ifPresent(
                            (user1) -> {
                                throw new IllegalStateException("email en uso");
                            });

            if (user.getEmail() != null
                    && user.getEmail().length() > 0
                    && !Objects.equals(user.getEmail(), newUser.getEmail())
                    && this.validarEmail(user.getEmail())) {

                newUser.setEmail(user.getEmail());
            }else throw new IllegalStateException("email invalido");
        }


        return newUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        AppUser user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "user with username " + username + " does not exists"));

        user.setAuthorities(
                user.getRole()
                        .getGrantedAuthorities());

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        HttpSession session = this.getSession();
        session.setAttribute("usersession", user);
        session.setAttribute("turnos", Turno.values());
        session.setAttribute("estados", EstadoNota.values());
        session.setAttribute("sexos", Sexo.values());
        session.setAttribute("admins", userRepository.findByRole(ADMIN));
        session.setAttribute("cursos", cursoRepository.findAll());
        session.setAttribute("duraciones", Duracion.values());
        session.setAttribute("anos", this.getYears());


        return user;
    }

    private List<String> getYears(){
        List<String> anos = new ArrayList<String>(10);
        for (int i=0;i<10;i++){
            anos.add(String.valueOf(now().getYear()+i));
        }
        return anos;
    }

    @Transactional
    public AppUser setRole(String dni, String role) throws ExcepcionServicio {
        AppUser user = userRepository
                .findByUsername(dni)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );
        user.setRole(valueOf(role));
        return user;
    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public AppUser setAlumnoToUser(AlumnoApp alumno, String userId) throws ExcepcionServicio {

        AppUser user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );
        user.setAlumno(alumno);
        return user;

    }

    @Transactional
    public AppUser setAdminToUser(AdminApp admin, AppUser user) {
        user.setAdmin(admin);
        return user;
    }

    @Transactional
    public void setEncodePW(String userId, String pw) throws ExcepcionServicio {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ExcepcionServicio("usuario no encontrado")
                );
        user.setPassword(passwordEncoder.encode(pw));
    }

    public HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }



}
