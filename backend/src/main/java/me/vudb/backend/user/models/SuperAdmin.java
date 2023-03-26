package me.vudb.backend.user.models;

import jakarta.persistence.*;
import me.vudb.backend.university.University;

import java.util.UUID;

@Entity
public class SuperAdmin {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @GeneratedValue(strategy = GenerationType.UUID)
    private String verification = UUID.randomUUID().toString();

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}