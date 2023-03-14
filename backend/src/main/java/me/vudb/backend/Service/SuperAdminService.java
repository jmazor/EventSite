package me.vudb.backend.Service;

import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.models.User;
import me.vudb.backend.repository.SuperAdminRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SuperAdminService {

    private final SuperAdminRepository superAdminRepository;
    private final UserService userService;

    public SuperAdminService(SuperAdminRepository superAdminRepository, UserService userService) {
        this.superAdminRepository = superAdminRepository;
        this.userService = userService;
    }

    public SuperAdmin createSuperAdmin(User user) {
        User savedUser = userService.save(user);
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setUser(savedUser);
        superAdmin.setVerification(UUID.randomUUID().toString());
        return superAdminRepository.save(superAdmin);
    }

    public SuperAdmin setVerificationCode(String superAdminId, String newVerificationCode) {
        SuperAdmin superAdmin = superAdminRepository.findById(superAdminId)
                .orElseThrow(() -> new EntityNotFoundException("SuperAdmin not found"));
        superAdmin.setVerification(newVerificationCode);
        return superAdminRepository.save(superAdmin);
    }

    public SuperAdmin findById(String id) {
        return superAdminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SuperAdmin not found"));
    }

    public List<SuperAdmin> findAll() {
        return superAdminRepository.findAll();
    }
}