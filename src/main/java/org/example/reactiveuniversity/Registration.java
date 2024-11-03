package org.example.reactiveuniversity;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.Instant;


public class Registration {

    @Id
    private long id;
    @Size(min = 3, message = "First name must by minimum 3 letter")
    private String firstName;
    @Size(min = 3, message = "Last name must by minimum 3 letter")
    private String lastName;
    @Email(message = "Enter valid email")
    private String email;
    private String password;
    @NotBlank(message = "Select role")
    private String role;
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant timeStamp;

    // @LastModifiedBy
    // private String lastModifiedBy;

    // @LastModifiedDate
    // private LocalDateTime lastModifiedDate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public @Size(min = 3, message = "First name must by minimum 3 letter") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 3, message = "First name must by minimum 3 letter") String firstName) {
        this.firstName = firstName;
    }

    public @Size(min = 3, message = "Last name must by minimum 3 letter") String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 3, message = "Last name must by minimum 3 letter") String lastName) {
        this.lastName = lastName;
    }

    public @Email(message = "Enter valid email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Enter valid email") String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public @NotBlank(message = "Select role") String getRole() {
        return role;
    }

    public void setRole(@NotBlank(message = "Select role") String role) {
        this.role = role;
    }
}
