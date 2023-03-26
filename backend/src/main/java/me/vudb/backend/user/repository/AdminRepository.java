package me.vudb.backend.user.repository;

import me.vudb.backend.user.models.Admin;
import me.vudb.backend.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findByUserEmail(String email);
}
