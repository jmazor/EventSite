package me.vudb.backend.service;
import me.vudb.backend.models.Rso;
import me.vudb.backend.repository.RsoRepository;

public class RsoService extends AbstractService<Rso, String>{
    private final RsoRepository rsoRepository;
    public RsoService(RsoRepository rsoRepository) {
        this.rsoRepository = rsoRepository;
    }
}
