package org.example.subject;

import org.springframework.data.annotation.Id;

public class Subject {


    @Id
    private long id;
    private String subject;

    public Subject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}

