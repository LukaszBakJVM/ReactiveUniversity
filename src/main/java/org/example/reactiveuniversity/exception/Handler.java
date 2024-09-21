package org.example.reactiveuniversity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error validation(CustomValidationException ex) {
        return new Error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error duplicateEmail(DuplicateEmailException ex) {
        return new Error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error userNotFound(UsernameNotFoundException ex) {
        return new Error(HttpStatus.FORBIDDEN, ex.getMessage());
    }
    @ExceptionHandler(ResponseStatusException.class)
    public Error handleResponseStatusException(ResponseStatusException ex) {
        return new Error(ex.getStatusCode(), ex.getMessage());
    }
}

