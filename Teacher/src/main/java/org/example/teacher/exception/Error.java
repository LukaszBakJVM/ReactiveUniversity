package org.example.teacher.exception;

import org.springframework.http.HttpStatus;

public record Error (HttpStatus status, String message){
}
