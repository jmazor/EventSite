package me.vudb.backend.controller;

import me.vudb.backend.models.LoginRequest;
import me.vudb.backend.models.Student;
import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.repository.StudentRepository;
import me.vudb.backend.repository.SuperAdminRepository;
import me.vudb.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is responsible for handling authentication requests
 *  In a more complicated program I would restructure this class to be more modular
 */

@RestController
@RequestMapping("/api/auth")
public class Auth {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (isValidUser(username, password)) {
            String token = JwtUtils.generateToken(studentRepository.findByEmail(username).getId(), "STUDENT");
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }
    @PostMapping("/admin/login")
    public ResponseEntity<String> adminLogin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (isValidAdmin(username, password)) {
            String token = JwtUtils.generateToken(superAdminRepository.findByEmail(username).getId(), "SUPER_ADMIN");
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    public boolean isValidUser(String username, String password) {
        Student student = studentRepository.findByEmail(username);
        if (student == null) {
            return false;
        }
        // Check if the password matches the user's password
        return student.getPassword().equals(password);
    }

    public boolean isValidAdmin(String username, String password) {
        SuperAdmin superAdmin = superAdminRepository.findByEmail(username);
        if (superAdmin == null) {
            return false;
        }
        // Check if verified
        if (superAdmin.getVerification() != null) {
            return false;
        }
        // Check if the password matches the user's password
        return superAdmin.getPassword().equals(password);
    }
}
