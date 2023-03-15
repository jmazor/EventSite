package me.vudb.backend.service;

import me.vudb.backend.models.Student;
import me.vudb.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends AbstractService<Student, String> {
    private final StudentRepository studentRepository;
    private final UserService userService;
    public StudentService(StudentRepository studentRepository, UserService userService) {
        this.studentRepository = studentRepository;
        this.userService = userService;
    }
}
