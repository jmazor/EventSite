package me.vudb.backend.repository;

import me.vudb.backend.models.user.SuperAdmin;
import me.vudb.backend.models.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, String> {
        University findByAdmin(SuperAdmin admin);
}
