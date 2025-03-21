package org.example.student;

import org.example.student.dto.*;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    Student dtoToStudent(WriteNewPerson writeNewPerson) {
        Student student = new Student();
        student.setFirstName(writeNewPerson.firstName());
        student.setLastName(writeNewPerson.lastName());
        student.setEmail(writeNewPerson.email());
        return student;
    }

    WriteNewPerson studentToDto(Student student) {
        return new WriteNewPerson(student.getFirstName(), student.getLastName(), student.getEmail());

    }

    AddCourse addCourseResponse(Student student) {
        return new AddCourse(student.getCourse(), student.getEmail());
    }


     StudentInfoWithCourse studentInfoWithCourse(Student student) {
        return new StudentInfoWithCourse(student.getFirstName(), student.getLastName(), student.getEmail(), student.getCourse());
    }
    StudentInfoWithoutCourse studentInfoWithoutCourse (Student student){
        return new StudentInfoWithoutCourse(student.getFirstName(),student.getLastName(),student.getEmail());
    }
    StudentEmail email(Student student){
        return new StudentEmail(student.getEmail());
    }
}
