package org.example.student;

import org.example.student.dto.WriteNewPerson;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    Student dtoToStudent(WriteNewPerson writeNewPerson){
        Student student = new Student();
        student.setFirstName(writeNewPerson.firstName());
        student.setLastName(writeNewPerson.lastName());
        student.setEmail(writeNewPerson.email());
        return student;
    }
    WriteNewPerson studentToDto(Student student){
        return new WriteNewPerson(student.getFirstName(), student.getLastName(), student.getEmail());
    }
}
