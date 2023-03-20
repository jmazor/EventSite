package me.vudb.backend.user.repository;

import me.vudb.backend.user.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByUserEmail(String email);
}
