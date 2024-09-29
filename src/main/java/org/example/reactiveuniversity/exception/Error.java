package org.example.reactiveuniversity.exception;

import org.springframework.http.HttpStatusCode;

public record Error(HttpStatusCode status, String message) {
}
