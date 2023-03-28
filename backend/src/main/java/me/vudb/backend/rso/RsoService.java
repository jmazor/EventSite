package me.vudb.backend.rso;
import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.university.University;
import me.vudb.backend.user.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public class RsoService  {
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

    public List<Rso> findByUniversityAndStatus(University university, boolean status) {
        return rsoRepository.findByUniversityAndStatus(university, status);
    }

    public Rso findById(String id) {
        return rsoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("RSO not found"));
    }

    public Rso save(Rso rso) {
        return rsoRepository.save(rso);
    }

    public Rso addUser(Rso rso, User user) {
        rso.getUsers().add(user);
        return rsoRepository.save(rso);
    }

    public List<Rso> findAllByAdmin(User user) {
        return rsoRepository.findAllByAdmin(user);
    }
}
