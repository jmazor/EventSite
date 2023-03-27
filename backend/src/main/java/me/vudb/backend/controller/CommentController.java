package me.vudb.backend.controller;

import me.vudb.backend.comment.Comments;
import me.vudb.backend.comment.CommentService;
import me.vudb.backend.event.EventService;
import me.vudb.backend.event.models.Event;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final EventService eventService;

    public CommentController(CommentService commentService, UserService userService, EventService eventService) {
        this.commentService = commentService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> commentOnEvent(@RequestBody Comments comments) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(comments.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            comments.setUser(user);
            comments.setEvent(event);
            commentService.save(comments);
            return ResponseEntity.ok("saved");
        } else {
            return ResponseEntity.badRequest().body("User or event not found.");
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editComment(@RequestBody Comments newCommentsData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(newCommentsData.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            Optional<Comments> optionalExistingComment = commentService.findById(newCommentsData.getId());
            if (optionalExistingComment.isPresent()) {
                Comments existingComments = optionalExistingComment.get();

                // Check if the authenticated user is the author of the comment
                if (existingComments.getUser().getId().equals(user.getId())) {
                    Comments updatedComments = commentService.editComment(existingComments, newCommentsData, user, event);
                    return ResponseEntity.ok(updatedComments);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to edit this comment.");
                }
            } else {
                return ResponseEntity.badRequest().body("Comment not found.");
            }
        } else {
            return ResponseEntity.badRequest().body("User or event not found.");
        }
    }



    @PostMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody Comments comments) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(comments.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            Optional<Comments> optionalExistingComment = commentService.findById(comments.getId());
            if (optionalExistingComment.isPresent()) {
                Comments existingComments = optionalExistingComment.get();

                // Check if the authenticated user is the author of the comment
                if (existingComments.getUser().getId().equals(user.getId())) {
                    commentService.delete(existingComments);
                    return ResponseEntity.ok("Comment deleted successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this comment.");
                }
            } else {
                return ResponseEntity.badRequest().body("Comment not found.");
            }
        } else {
            return ResponseEntity.badRequest().body("User or event not found.");
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getCommentsByEventId(@PathVariable String eventId) {
        System.out.println("test");
        Optional<Event> optionalEvent = eventService.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            return ResponseEntity.ok(event.getComments());
        } else {
            return ResponseEntity.badRequest().body("Event not found.");
        }
    }

}
