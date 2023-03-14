package me.vudb.backend.models;

public enum Role {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    STUDENT("Student");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

