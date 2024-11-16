package org.example.teacher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error validation(UsernameNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error connectionException(ConnectionException ex) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
    @ExceptionHandler(WrongCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Error wrongCredentials(WrongCredentialsException ex) {
        return new Error(HttpStatus.FORBIDDEN, ex.getMessage());
    }


}
