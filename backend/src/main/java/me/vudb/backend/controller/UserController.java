package me.vudb.backend.controller;

import me.vudb.backend.models.User;
import me.vudb.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path="/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        try {
            userRepository.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid request");
        }
    }

    @GetMapping(path="/add")
    public @ResponseBody Iterable<User> getAll(){
        return userRepository.findAll();
    }
}
