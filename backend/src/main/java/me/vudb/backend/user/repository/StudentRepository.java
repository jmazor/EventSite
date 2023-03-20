package me.vudb.backend.user.repository;

import me.vudb.backend.user.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByUserEmail(String email);

}