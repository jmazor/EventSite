package me.vudb.backend.comment;

// In a new file CommentRepository.java
import me.vudb.backend.event.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, String> {

    List<Comments> findAllByEvent(Event event);
}
