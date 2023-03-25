package me.vudb.backend.event.repository;

import me.vudb.backend.event.models.RsoEvent;
import me.vudb.backend.rso.Rso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RsoEventRepository extends JpaRepository<RsoEvent, String> {
    List<RsoEvent> findByRso(Rso rso);
}
