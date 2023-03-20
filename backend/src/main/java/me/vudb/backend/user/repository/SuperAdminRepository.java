package me.vudb.backend.user.repository;

import me.vudb.backend.user.models.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
    SuperAdmin findByUserEmail(String email);
}