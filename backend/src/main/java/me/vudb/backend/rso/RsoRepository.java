package me.vudb.backend.rso;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.university.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RsoRepository extends JpaRepository<Rso, String> {

    List<Rso> findByUniversity(University university);
    List<Rso> findByUniversityAndApproval(University university, boolean approval);
}

