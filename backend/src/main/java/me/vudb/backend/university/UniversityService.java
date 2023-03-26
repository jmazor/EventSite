package me.vudb.backend.university;

import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.user.models.SuperAdmin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public University save(University university) {
        return universityRepository.save(university);
    }

    public University findUniversityById(String id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public University findByAdmin(SuperAdmin superAdmin) {
        return universityRepository.findByAdmin(superAdmin);
    }
}
