package me.vudb.backend.controller;

import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.models.User;
import me.vudb.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/admin")
public class SuperAdminController {
    private final UserService superAdminService;

    public SuperAdminController(UserService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> createSuperAdmin(@RequestBody User user) {
        // Persist the SuperAdmin entity to the database
        SuperAdmin savedSuperAdmin = superAdminService.createSuperAdmin(user);

        return new ResponseEntity<>(savedSuperAdmin, HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<SuperAdmin> getAll(){
        return superAdminService.findAllAdmin();
    }

    /*@PostMapping("/createUniversity")
    public ResponseEntity<?> createUniversity(@RequestBody University university, @RequestHeader(value="Authorization") String authorizationHeader) {
        // Persist the University entity to the database
        String token = authorizationHeader.substring("Bearer ".length());
        String id = JwtUtils.getAdminId(token);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        // university.setAdmin(superAdminService.findById(id));
        // University savedUniversity = SuperAdminService.save(university);
        return new ResponseEntity<>(savedUniversity, HttpStatus.CREATED);
    }*/

}
