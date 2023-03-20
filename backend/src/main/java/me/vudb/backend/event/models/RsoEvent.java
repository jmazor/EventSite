package me.vudb.backend.event.models;

import jakarta.persistence.*;
import me.vudb.backend.rso.Rso;

@Entity
public class RsoEvent {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "rso_id", referencedColumnName = "id", nullable = false)
    private Rso rso;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "id")
    private Event event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Rso getRso() {
        return rso;
    }

    public void setRso(Rso rso) {
        this.rso = rso;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
