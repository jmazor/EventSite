package me.vudb.backend.rso;
import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.AbstractService;
import me.vudb.backend.university.University;
import me.vudb.backend.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class RsoService extends AbstractService<Rso, String> {
    private final RsoRepository rsoRepository;
    public RsoService(RsoRepository rsoRepository) {
        this.rsoRepository = rsoRepository;
    }

    public Set<User> findUsersByRsoId(String rsoId) {
        Rso rso = rsoRepository.findById(rsoId)
                .orElseThrow(() -> new EntityNotFoundException("RSO not found"));
        return rso.getUsers();
    }

    public List<Rso> findByUniversity(University university) {
        return rsoRepository.findByUniversity(university);
    }

    public List<Rso> findByUniversityAndApproval(University university, boolean approval) {
        return rsoRepository.findByUniversityAndApproval(university, approval);
    }
}
