package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.PrivateEvent;
import me.vudb.backend.university.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrivateEventRepository extends JpaRepository<PrivateEvent, String> {
    List<PrivateEvent> findByUniversity(University university);
}
