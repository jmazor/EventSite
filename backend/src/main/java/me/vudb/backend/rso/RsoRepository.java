package me.vudb.backend.rso;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RsoRepository extends JpaRepository<Rso, String> {

    Iterable<Rso> findByUniversity(University university);
}

