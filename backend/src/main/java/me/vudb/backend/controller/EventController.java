package me.vudb.backend.controller;

import me.vudb.backend.event.EventService;
import me.vudb.backend.event.models.PrivateEvent;
import me.vudb.backend.event.models.PublicEvent;
import me.vudb.backend.event.models.RsoEvent;
import me.vudb.backend.security.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<?> createPublicEvent(@RequestBody PublicEvent publicEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: ensure that the user is a admin
        String username = auth.getName();

        return null;
    }

    @PostMapping("/create/private")
    public ResponseEntity<?> createPublicEvent(@RequestBody PrivateEvent privateEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: ensure that the user is a SuperAdmin
        String username = auth.getName();
        return null;
    }

    @PostMapping("/create/rso")
    public ResponseEntity<?> createRsoEvent(@RequestBody RsoEvent rsoEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: ensure that the user is a admin
        String username = auth.getName();

        return null;
    }


}
