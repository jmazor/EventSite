package me.vudb.backend.university;

import jakarta.persistence.*;
import me.vudb.backend.models.user.SuperAdmin;

@Entity
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String location;

    private String description;

    private int numStudents;

    private String picture;

    private String emailDomain;

    // TODO: do I want this to be lazy?
    @ManyToOne()
    @JoinColumn(name = "admin_id", nullable = false)
    private SuperAdmin admin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public void setNumStudents(int numStudents) {
        this.numStudents = numStudents;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public SuperAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(SuperAdmin admin) {
        this.admin = admin;
    }
}
