package org.example.course;

import org.springframework.data.annotation.Id;

import java.util.Set;

public class Course {
    @Id
    private long id;
    private String courseName;
    private Set<String> subjectName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<String> getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(Set<String> subjectName) {
        this.subjectName = subjectName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
