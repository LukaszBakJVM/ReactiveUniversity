package org.example.student;

import org.example.student.dto.AddCourse;
import org.example.student.dto.StudentInfo;
import org.example.student.dto.WriteNewPerson;
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
    StudentInfo info(Student student){
        return new StudentInfo(student.getFirstName(),student.getLastName(),student.getEmail(),student.getCourse());
    }
}
