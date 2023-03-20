package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.PublicEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicEventRepository extends JpaRepository<PublicEvent, String> {
}
