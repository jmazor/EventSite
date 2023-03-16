package me.vudb.backend.service;
import jakarta.persistence.EntityNotFoundException;
import me.vudb.backend.models.Rso;
import me.vudb.backend.models.User;
import me.vudb.backend.repository.RsoRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class RsoService extends AbstractService<Rso, String>{
    private final RsoRepository rsoRepository;
    public RsoService(RsoRepository rsoRepository) {
        this.rsoRepository = rsoRepository;
    }
    public Set<User> findUsersByRsoId(String rsoId) {
        Rso rso = rsoRepository.findById(rsoId)
                .orElseThrow(() -> new EntityNotFoundException("RSO not found"));
        return rso.getUsers();
    }
}
