package org.example.reactiveuniversity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    private String lastName;
    @Email
    private String email;
    @Size(min = 6)
    private String password;
    @NotBlank
    private String role;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @Size(min = 3) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 3) String firstName) {
        this.firstName = firstName;
    }

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

    public @Size(min = 6) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6) String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
