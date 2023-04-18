package me.vudb.backend.controller;

import me.vudb.backend.event.EventService;
import me.vudb.backend.event.models.Event;
import me.vudb.backend.event.models.PrivateEvent;
import me.vudb.backend.event.models.PublicEvent;
import me.vudb.backend.event.models.RsoEvent;
import me.vudb.backend.rso.Rso;
import me.vudb.backend.security.JwtTokenUtil;
import me.vudb.backend.university.University;
import me.vudb.backend.university.UniversityService;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.Admin;
import me.vudb.backend.user.models.Student;
import me.vudb.backend.user.models.SuperAdmin;
import me.vudb.backend.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/api/event")
public class EventController {
    private final EventService eventService;

    private AuthenticationManager authenticationManager;

    private UniversityService universityService;

    private UserService userService;

    private JwtTokenUtil jwtTokenUtil;
    public EventController(EventService eventService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService, UniversityService universityService) {
        this.eventService = eventService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.universityService = universityService;
    }

    @PostMapping("/create/public")
    public ResponseEntity<?> createPublicEvent(@RequestBody Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        PublicEvent publicEvent = new PublicEvent();
        publicEvent.setEvent(event);


        publicEvent.setApproval(
                auth.getAuthorities().stream().anyMatch(
                        authority -> authority.getAuthority().equals("ROLE_SUPER_ADMIN")
                )
        );
        if (publicEvent.isApproval()) {
            publicEvent.setAdmin(userService.findSuperAdminByEmail(username));
        } else {
           userService.findByEmail(username).getEvents().add(event);
        }
        eventService.savePublic(publicEvent);

        return ResponseEntity.ok(publicEvent);

    }

    @PostMapping("/create/private")
    public ResponseEntity<?> createPrivateEvent(@RequestBody Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        SuperAdmin superAdmin = userService.findSuperAdminByEmail(username);
        University university = universityService.findByAdmin(superAdmin);
        PrivateEvent privateEvent = new PrivateEvent();
        privateEvent.setEvent(event);
        privateEvent.setUniversity(university);


        eventService.savePrivate(privateEvent);
        return ResponseEntity.ok(privateEvent);
    }


    @PostMapping("/create/rso")
    public ResponseEntity<?> createRsoEvent(@RequestBody RsoEvent rsoEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();
        Admin admin = userService.findAdminByUserEmail(username);
        Event event = rsoEvent.getEvent();
        rsoEvent.setRso(admin.getRso());
        admin.getUser().getEvents().add(rsoEvent.getEvent());
        eventService.saveRso(rsoEvent);

        return ResponseEntity.ok(rsoEvent);
    }

    @GetMapping("/joined")
    public ResponseEntity<List<Event>> getJoinedEvents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findByEmail(username);
        List<Event> eventList = new ArrayList<>(user.getEvents());

        return ResponseEntity.ok(eventList);
    }

    @GetMapping("/all")
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

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable String eventId){
        Optional<Event> optionalEvent = eventService.getEvent(eventId);
        if (optionalEvent.isPresent()) {
            return ResponseEntity.ok(optionalEvent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinEvent(@RequestBody Event event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println(event.getDescription());
        Optional<Event> optionalEvent = eventService.getEvent(event.getId());
        if (optionalEvent.isPresent()) {
            event = optionalEvent.get();
        } else {
            return ResponseEntity.notFound().build();
        }

        Optional<User> optionalUser = userService.findByEmailOpt(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            event.addUser(user);
            user.getEvents().add(event);
            eventService.save(event);
            userService.saveExisting(user);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }


    @GetMapping("/need/approval")
    public ResponseEntity<?> getEventsNeedingApproval() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<PublicEvent> events = eventService.findAllPublicEventsNeedingApproval();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveEvents(@RequestBody List<PublicEvent> publicEvents) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SuperAdmin superAdmin = userService.findSuperAdminByEmail(username);

        for (PublicEvent publicEvent : publicEvents) {
            publicEvent.setApproval(true);
            publicEvent.setAdmin(superAdmin);
            eventService.savePublic(publicEvent);
        }

        return ResponseEntity.ok("Approved");
    }

    /*
    @PostMapping("/rate")
    public ResponseEntity<?> rateEvent(@RequestBody Integer rating, @RequestParam String eventId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findByEmail(username);
        Event event = eventService.getEvent(eventId);
        if (event.getRating() == null) {
            event.setRating(rating);
        } else {
            event.setRating((event.getRating() + rating) / 2);
        }
        eventService.save(event);
        return ResponseEntity.ok("Rated");
    }

     */



}
