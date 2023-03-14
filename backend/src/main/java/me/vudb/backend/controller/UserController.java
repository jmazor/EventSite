package me.vudb.backend.controller;

import me.vudb.backend.Service.UserService;
import me.vudb.backend.models.User;
import me.vudb.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="/add")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAll(){
        return userService.findAll();
    }
}
