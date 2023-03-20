package me.vudb.backend.user.models;

import jakarta.persistence.*;
import me.vudb.backend.user.models.User;
import me.vudb.backend.university.University;

@Entity
public class Student {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "university_id")
    private University university;

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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
