package me.vudb.backend.controller;

import me.vudb.backend.comment.Comment;
import me.vudb.backend.comment.CommentService;
import me.vudb.backend.event.EventService;
import me.vudb.backend.event.models.Event;
import me.vudb.backend.user.UserService;
import me.vudb.backend.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path="/api/event")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;
    private final EventService eventService;

    public CommentController(CommentService commentService, UserService userService, EventService eventService) {
        this.commentService = commentService;
        this.userService = userService;
        this.eventService = eventService;
    }

    public ResponseEntity<?> commentOnEvent(@RequestBody Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(comment.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            comment.setUser(user);
            comment.setEvent(event);
            Comment savedComment = commentService.save(comment);
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.badRequest().body("User or event not found.");
        }
    }

    @PostMapping("/comment/edit")
    public ResponseEntity<?> editComment(@RequestBody Comment newCommentData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(newCommentData.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            Optional<Comment> optionalExistingComment = commentService.findById(newCommentData.getId());
            if (optionalExistingComment.isPresent()) {
                Comment existingComment = optionalExistingComment.get();

                // Check if the authenticated user is the author of the comment
                if (existingComment.getUser().getId().equals(user.getId())) {
                    Comment updatedComment = commentService.editComment(existingComment, newCommentData, user, event);
                    return ResponseEntity.ok(updatedComment);
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



    @PostMapping("/comment/delete")
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> optionalUser = userService.findByEmailOpt(email);
        Optional<Event> optionalEvent = eventService.findById(comment.getEvent().getId());

        if (optionalUser.isPresent() && optionalEvent.isPresent()) {
            User user = optionalUser.get();
            Event event = optionalEvent.get();

            Optional<Comment> optionalExistingComment = commentService.findById(comment.getId());
            if (optionalExistingComment.isPresent()) {
                Comment existingComment = optionalExistingComment.get();

                // Check if the authenticated user is the author of the comment
                if (existingComment.getUser().getId().equals(user.getId())) {
                    commentService.delete(existingComment);
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

}
