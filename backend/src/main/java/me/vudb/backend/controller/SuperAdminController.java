package me.vudb.backend.controller;

import me.vudb.backend.models.SuperAdmin;
import me.vudb.backend.models.University;
import me.vudb.backend.repository.SuperAdminRepository;
import me.vudb.backend.repository.UniversityRepository;
import me.vudb.backend.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/admin")
public class SuperAdminController {
    @Autowired
    private SuperAdminRepository superAdminRepository           ;

    @Autowired
    private UniversityRepository universityRepository;

    @PostMapping("/signUp")
    @Transactional
    public ResponseEntity<?> createSuperAdmin(@RequestBody SuperAdmin superAdmin) {
        // Persist the SuperAdmin entity to the database
        SuperAdmin savedSuperAdmin = superAdminRepository.save(superAdmin);

        return new ResponseEntity<>(savedSuperAdmin, HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<SuperAdmin> getAll(){
        return superAdminRepository.findAll();
    }

    @PostMapping("/createUniversity")
    public ResponseEntity<?> createUniversity(@RequestBody University university, @RequestHeader(value="Authorization") String authorizationHeader) {
        // Persist the University entity to the database
        String token = authorizationHeader.substring("Bearer ".length());
        String id = JwtUtils.getAdminId(token);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        university.setAdmin(superAdminRepository.findById(id).get());
        University savedUniversity = universityRepository.save(university);
        return new ResponseEntity<>(savedUniversity, HttpStatus.CREATED);
    }

}
