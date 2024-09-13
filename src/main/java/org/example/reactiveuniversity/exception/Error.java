package org.example.reactiveuniversity.exception;

import org.springframework.http.HttpStatus;

public record Error(HttpStatus status, String message) {
}
