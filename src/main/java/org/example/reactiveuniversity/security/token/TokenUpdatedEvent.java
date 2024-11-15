package org.example.reactiveuniversity.security.token;

import org.springframework.context.ApplicationEvent;

public class TokenUpdatedEvent extends ApplicationEvent {
    private final String fileName;


    public TokenUpdatedEvent(Object source, String fileName) {
        super(source);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
