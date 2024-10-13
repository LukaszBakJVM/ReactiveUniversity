package org.example.reactiveuniversity.exception;

public class WrongRoleException extends RuntimeException {
    public WrongRoleException(String message) {
        super(message);
    }
}
