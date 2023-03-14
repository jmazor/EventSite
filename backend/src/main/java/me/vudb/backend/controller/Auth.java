package me.vudb.backend.controller;

import me.vudb.backend.models.LoginRequest;
import me.vudb.backend.repository.UserRepository;
import me.vudb.backend.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserRepository userRepository;
    @Autowired
    private SuperAdminRepository superAdminRepository;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        return null;
    }
}
