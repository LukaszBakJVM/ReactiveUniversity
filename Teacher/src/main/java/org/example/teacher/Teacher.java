package org.example.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.Set;

public class Teacher {
    @Id
    private long id;
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    private String lastName;

    @Email
    private String email;
    private Set<String> subjectName;


    public @Size(min = 3) String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 3) String lastName) {
        this.lastName = lastName;
    }


    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }


    public Set<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(Set<String> subjectName) {
        this.subjectName = subjectName;
    }

    public @Size(min = 3) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 3) String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
