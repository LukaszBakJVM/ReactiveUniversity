package org.example.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Teacher {
    @Id
    private long id;
    @Size(min = 3)
    String lastName;
    @Min(value = 18, message = "Under 18")
    int age;
    @Email
    String email;
    @Size(min = 6)
    String password;
    List<String> subjectName;
    @Size(min = 3)
    private String firstName;

    public @Size(min = 3) String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 3) String lastName) {
        this.lastName = lastName;
    }

    @Min(value = 18, message = "Under 18")
    public int getAge() {
        return age;
    }

    public void setAge(@Min(value = 18, message = "Under 18") int age) {
        this.age = age;
    }

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public @Size(min = 6) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6) String password) {
        this.password = password;
    }

    public List<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(List<String> subjectName) {
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
