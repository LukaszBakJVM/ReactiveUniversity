package org.example.subject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {
    @ExceptionHandler(DuplicateSubjectException.class)
    public Error duplicateSubject(DuplicateSubjectException ex) {
        return new Error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SubjectNotFoundException.class)
    public Error subjectNotFound(SubjectNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
