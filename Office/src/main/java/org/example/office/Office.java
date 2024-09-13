package org.example.office;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public class Office {
    @Id
    private long id;
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    String lastName;
    @Email
   private String email;
    @Size(min = 6)
   private String password;


    private String role;

    public  String getLastName() {
        return lastName;
    }

    public void setLastName( String lastName) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}