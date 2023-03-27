package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

}
