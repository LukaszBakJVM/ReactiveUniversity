package org.example.subject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {
    @ExceptionHandler(DuplicateSubjectException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error duplicateSubject(DuplicateSubjectException ex) {
        return new Error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error subjectNotFound(SubjectNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
