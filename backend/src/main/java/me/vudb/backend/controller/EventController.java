package me.vudb.backend.controller;

import me.vudb.backend.event.EventService;
import me.vudb.backend.security.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/event")
public class EventController {
    private final EventService eventService;

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;
    public EventController(EventService eventService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.eventService = eventService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/create/public")
    public ResponseEntity<?> createPublicEvent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: ensure that the user is a admin
        String username = auth.getName();
        

        return null;
    }


}
