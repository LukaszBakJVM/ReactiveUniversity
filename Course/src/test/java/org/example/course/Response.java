package org.example.course;

import java.util.List;
import java.util.Set;

public class Response {
    String courseBySubject = """
            [{
                    "courseName": "course1",
                    "subjects": ["subject1","subject2","subject3"]
                    }]""";
    String course = """
            {
                    "courseName": "course2",
                    "subjects": ["subject4","subject5","subject6"]
                    }""";
    List<Course> save = List.of(delete(), save(), save1());


    private Course delete() {
        Course course = new Course();
        course.setCourseName("deleteCourse");
        course.setSubjectName(Set.of("delete", "delete1", "delete2"));
        return course;
    }

    private Course save() {
        Course course = new Course();
        course.setCourseName("course1");
        course.setSubjectName(Set.of("subject1", "subject2", "subject3"));
        return course;

    }

    private Course save1() {
        Course course = new Course();
        course.setCourseName("course2");
        course.setSubjectName(Set.of("subject4", "subject5", "subject6"));
        return course;

    }

    String courseNotFound = """
            {"status":"NOT_FOUND","message":"Course dontExist not found, delete error"}
            """;


}

