package me.vudb.backend.rso;

import me.vudb.backend.rso.Rso;
import me.vudb.backend.university.University;
import me.vudb.backend.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RsoRepository extends JpaRepository<Rso, String> {

    List<Rso> findByUniversity(University university);
    List<Rso> findByUniversityAndStatus(University university, boolean status);

    List<Rso> findAllByAdmin(User user);
}

