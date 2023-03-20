package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EventRepository extends JpaRepository<Event, String> {

}
