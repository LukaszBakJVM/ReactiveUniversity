package org.example.course.exception;

public class DuplicateCourseException extends RuntimeException {
    public DuplicateCourseException(String message) {
        super(message);
    }
}
