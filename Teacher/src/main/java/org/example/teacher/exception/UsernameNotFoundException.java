package org.example.teacher.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String message) {
        super(message);
    }
}
