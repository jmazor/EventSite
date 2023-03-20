package me.vudb.backend.university;

import me.vudb.backend.user.models.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, String> {
        University findByAdmin(SuperAdmin admin);
}
