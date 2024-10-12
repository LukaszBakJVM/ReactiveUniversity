package org.example.subject;

import org.springframework.data.annotation.Id;

public class Subject {


    public Subject(String subject) {
        this.subject = subject;
    }

    @Id
    private long id;
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

