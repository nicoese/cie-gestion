package com.example.demo.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;
import static java.time.LocalDate.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminApp createAdmin(AdminApp admin) {
        admin.setAlta(true);
        admin.setFechaAlta(now());
        return adminRepository.save(admin);
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

    @Transactional
    public AdminApp bajaAdmin(String adminId) {
        AdminApp admin = adminRepository
                .findById(adminId)
                .orElseThrow(
                        () -> new IllegalStateException("admin no encontrado")
                );
        return admin;
    }

    public Optional<AdminApp> findByDni(String dni) {
        return adminRepository.findByDni(dni);
    }
}
