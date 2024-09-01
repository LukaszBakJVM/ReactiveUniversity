package org.example.subject.exception;

public class SubjectNotFoundException extends RuntimeException{
    public SubjectNotFoundException(String message) {
        super(message);
    }
}
