package org.example.office.exception;

import org.springframework.http.HttpStatus;

public record Error (HttpStatus status, String message){
}
