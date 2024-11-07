package org.example.student.security.token;

import org.springframework.context.ApplicationEvent;

public class TokenUpdatedEvent extends ApplicationEvent {
    private String fileName;

    public TokenUpdatedEvent(Object source, String fileName) {
        super(source);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
