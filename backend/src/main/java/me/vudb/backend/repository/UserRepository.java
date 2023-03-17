package me.vudb.backend.repository;

import me.vudb.backend.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
