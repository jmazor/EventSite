package me.vudb.backend.comment;

import me.vudb.backend.event.models.Event;
import me.vudb.backend.user.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment editComment(Comment existingComment, Comment newCommentData, User user, Event event) {
        existingComment.setText(newCommentData.getText());
        existingComment.setUser(user);
        existingComment.setEvent(event);
        return commentRepository.save(existingComment);
    }

    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }
}
