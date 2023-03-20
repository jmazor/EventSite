package me.vudb.backend.controller;

import me.vudb.backend.university.University;
import me.vudb.backend.user.models.SuperAdmin;
import me.vudb.backend.user.models.User;
import me.vudb.backend.security.JwtTokenUtil;
import me.vudb.backend.user.UserService;
import me.vudb.backend.university.UniversityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/admin")
public class SuperAdminController {
    private final UserService superAdminService;

    private final UniversityService universityService;
    private final JwtTokenUtil jwtTokenUtil;


    public SuperAdminController(UserService superAdminService, UniversityService universityService) {
        this.superAdminService = superAdminService;
        this.jwtTokenUtil = new JwtTokenUtil();
        this.universityService = universityService;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> createSuperAdmin(@RequestBody User user) {
        SuperAdmin savedSuperAdmin = superAdminService.createSuperAdmin(user);
        return new ResponseEntity<>(savedSuperAdmin, HttpStatus.CREATED);
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<SuperAdmin> getAll(){
        return superAdminService.findAllAdmin();
    }

    @PostMapping("/createUniversity")
    public ResponseEntity<?> createUniversity(@RequestBody University university) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        university.setAdmin(superAdminService.findSuperAdminByUserId(username));
        University savedUniversity = universityService.save(university);
        return new ResponseEntity<>(savedUniversity, HttpStatus.CREATED);
    }

}
