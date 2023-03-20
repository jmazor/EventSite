package me.vudb.backend.controller;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.rso.RsoService;
import me.vudb.backend.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/rso")
public class RsoController {
    private final RsoService rsoService;
    private final UserService userService;
    public RsoController(RsoService rsoService, UserService userService) {
        this.rsoService = rsoService;
        this.userService = userService;
    }


    @PostMapping("create")
    public void createRso(@RequestBody Rso rso) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Ensure that approval is false
        rso.setApproval(false);
        rso.setAdmin(userService.findByEmail(username));
        rsoService.save(rso);
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Rso> getAll() {
        return rsoService.findAll();
    }
}
