package me.vudb.backend.controller;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.rso.RsoService;
import me.vudb.backend.security.JwtTokenUtil;
import me.vudb.backend.university.University;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        // Ensure that approval is false
        rso.setApproval(false);
        rso.setAdmin(userService.findByEmail(username));
        rsoService.save(rso);
        return ResponseEntity.ok(rso);
    }

    @PostMapping("/join")
    public ResponseEntity<Rso> joinRso(@RequestBody Rso rso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ensure that approval is false
        rso.setApproval(false);
        rso.setAdmin(userService.findByEmail(username));
        rsoService.save(rso);
        return ResponseEntity.ok(rso);
    }

    @GetMapping("/all")
    public @ResponseBody List<Rso> getAll(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        University university = userService.findStudentUniversityByEmail(username);
        return rsoService.findByUniversityAndApproval(university, true);
    }
}
