package org.example.reactiveuniversity.registration;

public enum Role {
    OFFICE("Ofice"),TEACHER("Teacher"),STUDENT("Student");
    private final String ROLE;

    Role(String role) {
        ROLE = role;
    }

    public String getROLE() {
        return ROLE;
    }
}
