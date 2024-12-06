package org.example.student;

import org.example.student.dto.AddCourse;
import org.example.student.grades.dto.GradesRequest;

import java.util.List;

public class Response {

    String findStudentByCourse = """
    [
        {
            "firstName": "student4",
            "lastName": "StudentLastName4",
            "email": "student4@email.com",
            "course": "Biol-Chem"
        },
        {
            "firstName": "student3",
            "lastName": "StudentLastName3",
            "email": "student3@email.com",
            "course": "Biol-Chem"
        }
        ]
    """;

    String myTeachers = """
            [{"firstName":"Teacher2","lastName":"Bak"},{"firstName":"Teacher1","lastName":"Bak"}]
            """;

    GradesRequest gradesRequest(){
        return new GradesRequest("student3@email.com","Chemia","5+ Sprawdzian");

    }



    AddCourse addCourse() {
        return new AddCourse("Medycyna", "student@email.com");
    }


    List<Student>saveStudents(){
        return List.of(save(),save2(),save3(),save4(),save1(),save5());
    }

    Student save() {
        Student student = new Student();
        student.setFirstName("student");
        student.setLastName("StudentLastName");
        student.setEmail("student@email.com");
        return student;
    }
    Student save1() {
        Student student = new Student();
        student.setFirstName("student1");
        student.setLastName("StudentLastName1");
        student.setEmail("student1@email.com");
        return student;
    }
    Student save2() {
        Student student = new Student();
        student.setFirstName("student2");
        student.setLastName("StudentLastName2");
        student.setEmail("student2@email.com");
        return student;
    }
    Student save3() {
        Student student = new Student();
        student.setFirstName("student3");
        student.setLastName("StudentLastName3");
        student.setEmail("student3@email.com");
        student.setCourse("Biol-Chem");
        return student;
    }
    Student save4() {
        Student student = new Student();
        student.setFirstName("student4");
        student.setLastName("StudentLastName4");
        student.setEmail("student4@email.com");
        student.setCourse("Biol-Chem");
        return student;
    }
    Student save5() {
        Student student = new Student();
        student.setFirstName("Student4");
        student.setLastName("Bak");
        student.setEmail("student1@interia.pl");
        student.setCourse("Elektrotechnika i Informatyka");
        return student;
    }
}
