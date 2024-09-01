package org.example.subject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {
    @ExceptionHandler(DuplicateSubjectException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error userNotFoundException(DuplicateSubjectException ex) {
        return new Error(HttpStatus.CONFLICT.value(), ex.getMessage());
    }
}
