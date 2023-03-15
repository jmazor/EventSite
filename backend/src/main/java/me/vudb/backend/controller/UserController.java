package me.vudb.backend.controller;

import me.vudb.backend.service.UserService;
import me.vudb.backend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="/add")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @GetMapping(path="/{email}")
    public @ResponseBody User getUserByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAll(){
        return userService.findAll();
    }

    @GetMapping("/test")
    public ResponseEntity<String> printRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("No role found");

        return ResponseEntity.ok().body("Role: " + role);
    }
}
