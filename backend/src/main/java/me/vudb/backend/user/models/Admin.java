package me.vudb.backend.user.models;

import jakarta.persistence.*;
import me.vudb.backend.rso.Rso;

@Entity
public class Admin {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "rso_id")
    private Rso rso;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Rso getRso() {
        return rso;
    }

    public void setRso(Rso rso) {
        this.rso = rso;
    }
}
