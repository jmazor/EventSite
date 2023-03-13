package me.vudb.backend.controller;

import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.models.User;
import me.vudb.backend.repository.SuperAdminRepository;
import me.vudb.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/admin")
public class SuperAdminController {
    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> createSuperAdmin(@RequestBody User user) {
        // Create the User entity and save it to the database
        User savedUser = userRepository.save(user);

        // Create the SuperAdmin entity and set the User relationship
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setUser(savedUser);

        // Persist the SuperAdmin entity to the database
        SuperAdmin savedSuperAdmin = superAdminRepository.save(superAdmin);

        return new ResponseEntity<>(savedSuperAdmin, HttpStatus.CREATED);
    }



    @GetMapping(path="/all")
    public @ResponseBody Iterable<SuperAdmin> getAll(){
        return superAdminRepository.findAll();
    }
}
