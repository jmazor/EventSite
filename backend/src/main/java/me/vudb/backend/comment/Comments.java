package me.vudb.backend.comment;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import me.vudb.backend.event.models.Event;
import me.vudb.backend.user.models.User;

import java.time.LocalDateTime;

@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String text;

    private LocalDateTime date = LocalDateTime.now();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}