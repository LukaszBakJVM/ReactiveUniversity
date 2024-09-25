package org.example.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {


    @ExceptionHandler(DuplicateCourseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error duplicateEmail(DuplicateCourseException ex) {
        return new Error(HttpStatus.CONFLICT, ex.getMessage());
    }
}
