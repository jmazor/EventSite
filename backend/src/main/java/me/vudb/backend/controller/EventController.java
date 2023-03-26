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
import me.vudb.backend.user.models.Student;
import me.vudb.backend.user.models.SuperAdmin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
        // TODO: ensure that the user is a admin
        String username = auth.getName();

        return null;
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

    @GetMapping("/join/{eventId}")
    public ResponseEntity<?> joinEvent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return null;

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

}
