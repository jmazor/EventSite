package me.vudb.backend.controller;

import me.vudb.backend.models.Rso;
import me.vudb.backend.service.RsoService;
import me.vudb.backend.service.UserService;
import me.vudb.backend.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
    private final UserService userService;
    private final RsoService rsoService;
    public UserController(UserService userService, RsoService rsoService) {
        this.userService = userService;
        this.rsoService = rsoService;
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
    public ResponseEntity<?> testEndpoint(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().orElseThrow().getAuthority();
        return ResponseEntity.ok().body("{\"message\": \"Role: " + role + "\"}");
    }

    @GetMapping("/rso/{userId}")
    public ResponseEntity<Set<Rso>> getRsoForUser(@PathVariable String userId) {
        Set<Rso> rsos = userService.findUserRso(userId);
        return new ResponseEntity<>(rsos, HttpStatus.OK);
    }

    @GetMapping("/rso/users/{rsoId}")
    public ResponseEntity<Set<User>> getUsersForRso(@PathVariable String rsoId) {
        Set<User> users = rsoService.findUsersByRsoId(rsoId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
