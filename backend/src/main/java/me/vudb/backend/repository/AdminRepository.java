package me.vudb.backend.repository;

import me.vudb.backend.models.Student;
import me.vudb.backend.models.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByUserEmail(String email);
}
