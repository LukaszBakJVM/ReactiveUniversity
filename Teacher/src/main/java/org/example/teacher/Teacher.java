package org.example.teacher;

import org.springframework.data.annotation.Id;

import java.util.Set;

public class Teacher {
    @Id
    private long id;

    private String firstName;

    private String lastName;


    private String email;
    private Set<String> subjectName;


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(Set<String> subjectName) {
        this.subjectName = subjectName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
