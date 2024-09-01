package org.example.teacher;

import jakarta.validation.ConstraintViolation;
import org.example.teacher.exception.ValidationException;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.stream.Collectors;

public class TeacherServices {
    private final LocalValidatorFactoryBean validation;

    public TeacherServices(LocalValidatorFactoryBean validation) {
        this.validation = validation;
    }

    private void validationTeacher(Teacher teacher) {
        Set<ConstraintViolation<Teacher>> violations = validation.validate(teacher);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; ", "Validation error occurred: ", ""));

            throw new ValidationException(errorMessage);
        }
    }
}
