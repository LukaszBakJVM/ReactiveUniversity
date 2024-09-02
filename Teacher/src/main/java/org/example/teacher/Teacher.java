package org.example.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

public record Teacher(@Size(min = 3) String firstName, @Size(min = 3) String lastName, @Min(value = 18,message = "Under 18") int age,
                      @Email String email, List<String> subjectName) {
}
