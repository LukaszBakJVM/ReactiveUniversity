package org.example.course.exception;

import org.springframework.http.HttpStatus;

public record Error (HttpStatus status, String message){
}
