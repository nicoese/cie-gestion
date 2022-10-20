package com.example.demo.admin;

import com.example.demo.enums.Sexo;
import com.example.demo.enums.UserRole;
import com.example.demo.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.example.demo.enums.UserRole.ADMIN;
import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminApp createAdmin(String nombre,
                                String apellido,
                                Sexo sexo) {
        AdminApp admin = new AdminApp();
        admin.setNombre(nombre);
        admin.setApellido(apellido);
        admin.setAlta(true);
        admin.setFechaAlta(now());
        admin.setSexo(sexo);
        return adminRepository.save(admin);
    }

    @Transactional
    public void setAdminToUser(AppUser user,
                               AdminApp admin){
        user.setAdmin(admin);
        user.setRole(ADMIN);
    }

    public Optional<AdminApp> readAdmin(String adminId) {
        return adminRepository.findById(adminId);
    }

    @Transactional
    public AdminApp updateAdmin(AdminApp admin, String adminId) {
        AdminApp antiguoAdmin = adminRepository
                .findById(adminId)
                .orElseThrow(
                        () -> new IllegalStateException("admin no encontrado")
                );
        return admin;
    }

    public void deleteAdmin(String adminId) {
        adminRepository.deleteById(adminId);
    }

}
