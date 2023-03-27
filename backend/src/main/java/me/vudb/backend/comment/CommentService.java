package me.vudb.backend.comment;

import me.vudb.backend.event.models.Event;
import me.vudb.backend.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comments save(Comments comments) {
        return commentRepository.save(comments);
    }

    public void delete(Comments comments) {
        commentRepository.delete(comments);
    }

    public Comments editComment(Comments existingComments, Comments newCommentsData, User user, Event event) {
        existingComments.setText(newCommentsData.getText());
        existingComments.setUser(user);
        existingComments.setEvent(event);
        return commentRepository.save(existingComments);
    }

    public Optional<Comments> findById(String id) {
        return commentRepository.findById(id);
    }

    public List<Comments> findAllByEvent(Event event) {
        return commentRepository.findAllByEvent(event);
    }
}
