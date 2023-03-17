package me.vudb.backend.repository;

import me.vudb.backend.models.user.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
    SuperAdmin findByUserEmail(String email);
}