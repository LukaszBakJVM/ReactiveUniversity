package org.example.course;

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


    Course delete() {
        Course course = new Course();
        course.setCourseName("deleteCourse");
        course.setSubjectName(Set.of("delete", "delete1", "delete2"));
        return course;
    }

    String courseNotFound = """
            {"status":"NOT_FOUND","message":"Course dontExist not found, delete error"}
            """;


}

