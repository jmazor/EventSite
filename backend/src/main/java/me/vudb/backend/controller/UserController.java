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

    @GetMapping("/rso/users/{rsoId}")
    public ResponseEntity<Set<User>> getUsersForRso(@PathVariable String rsoId) {
        Set<User> users = rsoService.findUsersByRsoId(rsoId);
        return new ResponseEntity<>(users, HttpStatus.OK);
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

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getPossibleUserEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Retrieve the Student object associated with the User
        Student student = userService.findStudentByEmail(username);

        // Retrieve the University object associated with the Student
        University university = student.getUniversity();

        // Retrieve all public events
        List<PublicEvent> publicEvents = eventService.findAllApprovedPublicEvents();

        // Retrieve all private events associated with the user's university
        List<PrivateEvent> privateEvents = eventService.findAllPrivateEvent(university);

        // Retrieve all RSO events for the user's registered RSOs
        List<RsoEvent> rsoEvents = new ArrayList<>();
        for (Rso rso : student.getUser().getRso()) {
            rsoEvents.addAll(eventService.findByRso(rso));
        }

        // Convert the public, private, and RSO events to a list of Event objects
        List<Event> eventList = new ArrayList<>();
        publicEvents.forEach(publicEvent -> eventList.add(publicEvent.getEvent()));
        privateEvents.forEach(privateEvent -> eventList.add(privateEvent.getEvent()));
        rsoEvents.forEach(rsoEvent -> eventList.add(rsoEvent.getEvent()));

        // Return the combined list of events
        return ResponseEntity.ok(eventList);
    }
}
