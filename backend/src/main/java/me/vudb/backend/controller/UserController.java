package me.vudb.backend.controller;

import me.vudb.backend.models.Student;
import me.vudb.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api")
public class UserController {
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping(path="/add")
    public ResponseEntity<Student> addNewUser(@RequestBody Student student) {
        studentRepository.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<Student> getAll(){
        return studentRepository.findAll();
    }
}
