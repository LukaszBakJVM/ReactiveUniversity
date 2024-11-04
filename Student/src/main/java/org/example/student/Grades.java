package org.example.student;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grades {
    @Id
    private long id;
    private Map<String, List<String>> subjectAndGrades = new HashMap<>();
    private String studentEmail;
    @CreatedDate
    private LocalDate timeStamp;
    @CreatedBy
    private String teacher;


}
