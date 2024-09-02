package org.example.teacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error validation(ValidationException ex) {
        return new Error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error duplicateEmail(DuplicateEmailException ex) {
        return new Error(HttpStatus.CONFLICT, ex.getMessage());
    }
}
