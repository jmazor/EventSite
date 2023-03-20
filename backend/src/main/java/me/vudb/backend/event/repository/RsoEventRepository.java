package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.RsoEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RsoEventRepository extends JpaRepository<RsoEvent, String> {
}
