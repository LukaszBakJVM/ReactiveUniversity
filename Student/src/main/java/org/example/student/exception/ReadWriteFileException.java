package org.example.student.exception;

public class ReadWriteFileException extends RuntimeException{
    public ReadWriteFileException(String message) {
        super(message);
    }
}
