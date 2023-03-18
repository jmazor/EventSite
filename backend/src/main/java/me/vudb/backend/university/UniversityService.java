package me.vudb.backend.university;

import me.vudb.backend.service.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public University save(University university) {
        return universityRepository.save(university);
    }

}
