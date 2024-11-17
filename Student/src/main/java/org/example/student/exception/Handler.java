package org.example.student.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {


    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error notFound(UsernameNotFoundException ex) {
        return new Error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConnectionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error connectionException(ConnectionException ex) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
    }
    @ExceptionHandler(ReadWriteFileException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public Error fileException(ReadWriteFileException ex) {
        return new Error(HttpStatus.FAILED_DEPENDENCY, ex.getMessage());
    }


}
