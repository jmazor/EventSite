package me.vudb.backend.controller;

import me.vudb.backend.event.EventService;
import me.vudb.backend.event.models.Event;
import me.vudb.backend.event.models.PrivateEvent;
import me.vudb.backend.event.models.PublicEvent;
import me.vudb.backend.event.models.RsoEvent;
import me.vudb.backend.rso.Rso;
import me.vudb.backend.rso.RsoService;
import me.vudb.backend.university.University;
import me.vudb.backend.university.UniversityService;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.Student;
import me.vudb.backend.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="/api/user")
public class UserController {
    private final UserService userService;
    private final RsoService rsoService;
    private final EventService eventService;

    private final UniversityService universityService;
    public UserController(UserService userService, RsoService rsoService, UniversityService universityService, EventService eventService) {
        this.userService = userService;
        this.rsoService = rsoService;
        this.universityService = universityService;
        this.eventService = eventService;
    }

    @PostMapping(path="/register")
    public ResponseEntity<Student> addNewStudent(@RequestBody Student student) {
        student = userService.createStudent(student.getUser(), universityService.findUniversityById(student.getUniversity().getId()));
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }
    @GetMapping(path="/{email}")
    public @ResponseBody User getUserByEmail(@PathVariable String email){
        return userService.findByEmail(email);
    }
    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAll(){
        return userService.findAll();
    }


    @GetMapping("/rso/{userId}")
    public ResponseEntity<Set<Rso>> getRsoForUser(@PathVariable String userId) {
        Set<Rso> rsos = userService.findUserRso(userId);
        return new ResponseEntity<>(rsos, HttpStatus.OK);
    }

    @GetMapping("/rso")
    public ResponseEntity<Set<Rso>> getRsoForUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByEmail(username);
        Set<Rso> rsos = user.getRso();
        return new ResponseEntity<>(rsos, HttpStatus.OK);
    }

    @GetMapping("/events/registered")
    public ResponseEntity<List<Event>> getUserEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Retrieve the user object for the given username
        User user = userService.findByEmail(username);

        // Retrieve the list of events for the user
        Set<Event> events = user.getEvents();

        // Convert the set of events to a list and return it
        List<Event> eventList = new ArrayList<>(events);
        return ResponseEntity.ok(eventList);
    }

}
