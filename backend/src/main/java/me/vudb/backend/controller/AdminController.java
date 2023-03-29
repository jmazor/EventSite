package me.vudb.backend.controller;


import me.vudb.backend.rso.Rso;
import me.vudb.backend.rso.RsoService;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api/rsoadmin")
public class AdminController {
    private final UserService userService;
    private final RsoService rsoService;

    public AdminController(UserService userService, RsoService rsoService) {
        this.userService = userService;
        this.rsoService = rsoService;
    }

    @GetMapping("/rso")
    public @ResponseBody List<Rso> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByEmail(username);
        return rsoService.findAllByAdmin(user);
    }
}
