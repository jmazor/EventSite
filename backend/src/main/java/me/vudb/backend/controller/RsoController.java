package me.vudb.backend.controller;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.rso.RsoService;
import me.vudb.backend.security.JwtTokenUtil;
import me.vudb.backend.university.University;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.Student;
import me.vudb.backend.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="/api/rso")
public class RsoController {
    private final RsoService rsoService;
    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public RsoController(RsoService rsoService, UserService userService) {
        this.rsoService = rsoService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<Rso> createRso(@RequestBody Rso rso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Student student = userService.findStudentByEmail(username);

        // Ensure that approval is false
        rso.setStatus(false);
        rso.setUniversity(student.getUniversity());
        rso.setAdmin(student.getUser());
        student.getUser().getRso().add(rso);
        rsoService.save(rso);
        return ResponseEntity.ok(rso);
    }

    @PostMapping("/join")
    public ResponseEntity<Rso> joinRso(@RequestBody Rso rso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        rso = rsoService.findById(rso.getId());
        User user = userService.findByEmail(username);
        user.getRso().add(rso);
        userService.saveExisting(user);
        return ResponseEntity.ok(rso);
    }


    @GetMapping("/all")
    public @ResponseBody List<Rso> getAll(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        University university = userService.findStudentUniversityByEmail(username);
        return rsoService.findByUniversityAndStatus(university, true);
    }

    // TODO: NO IDEA IF THIS WORKS
    @GetMapping("/pending")
    public @ResponseBody List<Rso> getPending(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        University university = userService.findStudentUniversityByEmail(username);
        return rsoService.findByUniversityAndStatus(university, false);
    }

    @GetMapping("/{rsoId}")
    public ResponseEntity<Set<User>> getUsersForRso(@PathVariable String rsoId) {
        Set<User> users = rsoService.findUsersByRsoId(rsoId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
