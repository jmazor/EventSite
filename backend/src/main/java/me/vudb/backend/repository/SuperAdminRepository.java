package me.vudb.backend.repository;

import me.vudb.backend.models.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, String> {
    SuperAdmin findByEmail(String username);
}