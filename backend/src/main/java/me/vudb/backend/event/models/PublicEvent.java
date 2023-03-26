package me.vudb.backend.event.models;

import jakarta.persistence.*;
import me.vudb.backend.user.models.SuperAdmin;

@Entity
public class PublicEvent {
    @Id
    private String id;

    private boolean approval = false;

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private SuperAdmin admin;

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

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public SuperAdmin getAdmin() {
        return admin;
    }

    public void setAdmin(SuperAdmin admin) {
        this.admin = admin;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }


}
