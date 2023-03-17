package me.vudb.backend.controller;

import me.vudb.backend.models.user.User;
import me.vudb.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import me.vudb.backend.security.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        logger.info("Received login request: {}", loginRequest);
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtil.generateJwtToken(authentication);

            return ResponseEntity.ok().body("Token: " + jwtToken);
        } catch (Exception e) {
            logger.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok().body("Test endpoint works!");
    }
}


