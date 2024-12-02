package org.example.student;

import org.example.student.dto.AddCourse;

public class Response {

    AddCourse addCourse() {
        return new AddCourse("Medycyna", "student@email.com");
    }


    Student saveToCourse() {
        Student student = new Student();
        student.setFirstName("student");
        student.setLastName("StudentLastName");
        student.setEmail("student@email.com");
        return student;

    }
}
