package org.example.student.grades;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Grades {
    @Id
    private long id;
    private String email;
    private String subject;

    private List<String> gradesDescription = new ArrayList<>();
    @CreatedDate
    private LocalDate timeStamp;
    private String teacher;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public List<String> getGradesDescription() {
        return gradesDescription;
    }

    public void setGradesDescription(List<String> gradesDescription) {
        this.gradesDescription = gradesDescription;
    }

    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}


