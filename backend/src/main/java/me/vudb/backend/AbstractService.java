package me.vudb.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class AbstractService<T, ID> {
    private final JpaRepository<T, ID> repository;

    protected AbstractService() {
        this.repository = null;
    }
    protected AbstractService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
