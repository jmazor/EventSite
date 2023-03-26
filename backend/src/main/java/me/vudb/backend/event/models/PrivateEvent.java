package me.vudb.backend.event.models;

import jakarta.persistence.*;
import me.vudb.backend.university.University;
@Entity
public class PrivateEvent {
    @Id
    private String id;

    @ManyToOne()
    @JoinColumn(name = "university_id", referencedColumnName = "id", nullable = false)
    private University university;

    @OneToOne()
    @MapsId
    @JoinColumn(name = "id")
    Event event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

}
