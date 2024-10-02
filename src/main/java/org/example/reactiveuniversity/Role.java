package org.example.reactiveuniversity;

public enum Role {
    OFFICE("Office"), TEACHER("Teacher"), STUDENT("Student");
    private final String ROLE;

    Role(String role) {
        ROLE = role;
    }

    public String getROLE() {
        return ROLE;
    }
}
