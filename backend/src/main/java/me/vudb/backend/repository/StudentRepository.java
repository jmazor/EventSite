package me.vudb.backend.repository;

import me.vudb.backend.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByUserEmail(String email);

}