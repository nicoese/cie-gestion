package com.example.demo.user;

import com.example.demo.admin.AdminApp;
import com.example.demo.alumno.AlumnoApp;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo.enums.UserRole.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AppUser> getAppUsers() {
        return userRepository.findAll();
    }

    public Optional<AppUser> getOne(String id) {
        return userRepository.findById(id);
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

    public void validarEmail(String email) throws ExcepcionServicio {

        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }

        if (!result) {
            throw new ExcepcionServicio("email invalido");
        }
    }

    public void validarUsername(String username) throws ExcepcionServicio{

        if (username.length()<7 || username.length()>8){
            throw new ExcepcionServicio("dni invalido");
        }

        try {
            Long.parseLong(username);
        }catch (NumberFormatException e){
            throw new ExcepcionServicio("dni invalido");
        }

    }

    public String generatePW(){
        return PasswordConfig.generatePassayPassword();
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public AppUser updateUser(String id, AppUser user) throws ExcepcionServicio {

        AppUser newUser = userRepository.findById(id)
                .orElseThrow(() -> new ExcepcionServicio("user with id " + id + " does not exists"));

        if (user.getEmail() != null
                && user.getEmail().length() > 0
                && !Objects.equals(user.getEmail(), newUser.getEmail())) {

            Optional<AppUser> optionalUser = userRepository.findByEmail(user.getEmail());

            if (optionalUser.isPresent()) throw new ExcepcionServicio("email taken");

            newUser.setEmail(user.getEmail());
        }
        if (user.getUsername() != null
                && user.getUsername().length() > 0
                && !Objects.equals(user.getUsername(), newUser.getUsername())) {

            if (userRepository.
                    findByUsername(user.getUsername())
                    .isPresent()) throw new ExcepcionServicio("email taken");

            newUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null
                && user.getPassword().length() > 0
                && !Objects.equals(user.getPassword(), newUser.getPassword())) {

            newUser.setPassword(user.getPassword());
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

        HttpSession session = this.getSession();
        session.setAttribute("usersession", user);


        return user;
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
    public AppUser setAdminToUser(AdminApp admin, AppUser user){
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

    public HttpSession getSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }




}
