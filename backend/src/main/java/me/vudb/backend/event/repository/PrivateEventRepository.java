package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.PrivateEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateEventRepository extends JpaRepository<PrivateEvent, String> {

}
