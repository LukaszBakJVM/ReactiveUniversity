package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;

import java.util.HashSet;
import java.util.Set;

public class Response {

    String findMyStudents = """
                        [
                {
                    "student": {
                        "firstName": "Student5",
                        "lastName": "Bak",
                        "email": "student5@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student20",
                        "lastName": "Bak",
                        "email": "student20@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student",
                        "lastName": "Bak",
                        "email": "student@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student18",
                        "lastName": "Bak",
                        "email": "student18@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student17",
                        "lastName": "Bak",
                        "email": "student17@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student7",
                        "lastName": "Bak",
                        "email": "student7@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student13",
                        "lastName": "Bak",
                        "email": "student13@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student14",
                        "lastName": "Bak",
                        "email": "student14@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student19",
                        "lastName": "Bak",
                        "email": "student19@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student16",
                        "lastName": "Bak",
                        "email": "student16@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student8",
                        "lastName": "Bak",
                        "email": "student8@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student9",
                        "lastName": "Bak",
                        "email": "student9@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student6",
                        "lastName": "Bak",
                        "email": "student6@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student11",
                        "lastName": "Bak",
                        "email": "student11@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": [
                        {
                            "subject": "Anatomia ",
                            "grades": [
                                "  5 odpowiedz  2024-11-12",
                                "  5 sprawdzian  2024-11-12"
                            ]
                        }
                    ]
                },
                {
                    "student": {
                        "firstName": "Student12",
                        "lastName": "Bak",
                        "email": "student12@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": [
                        {
                            "subject": "Anatomia ",
                            "grades": [
                                "  5 sprawdzian  2024-11-12",
                                "  5 sprawdzian  2024-11-12"
                            ]
                        }
                    ]
                },
                {
                    "student": {
                        "firstName": "Student10",
                        "lastName": "Bak",
                        "email": "student10@interia.pl",
                        "course": "Matematyczno-Geograficzny"
                    },
                    "grades": []
                },
                {
                    "student": {
                        "firstName": "Student15",
                        "lastName": "Bak",
                        "email": "student15@interia.pl",
                        "course": "Medycyna"
                    },
                    "grades": []
                }
            ]""";

    String teacherPrivateInfo = """
                      {
                "firstName": "teacher1",
                "lastName": "Bak",
                "email": "teacher1@interia.pl",
                "subjectName": [
                    "Biologia",
                     "Fizyka"
                ]
            }""";

    String allTeachers = """
            [
             {
                    "firstName": "teacher4",
                    "lastName": "Bak",
                    "email": "teacher4@interia.pl",
                    "subjectName": [
                        "Język Angielski Medyczny",
                        "Język Angielski"
                    ]
                },
                {
                    "firstName": "teacher1",
                    "lastName": "Bak",
                    "email": "teacher1@interia.pl",
                    "subjectName": [
                        "Fizyka",
                        "Biologia"
            
                    ]
                }
            
            
            
            ]""";


    String infoSubject = """

            {
                "firstName":"Teacher2",
                    "lastName":"Bak",
                    "subjectName": [
                "Java",
                        "C++",
                        "Anatomia",
                        "Chemia",
                        "Biologia"
                        ]
            }
            """;


    Teacher saveTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("teacher4");
        teacher.setLastName("Bak");
        teacher.setEmail("teacher4@interia.pl");
        teacher.setSubjectName(Set.of("Język Angielski Medyczny", "Język Angielski"));
        return teacher;
    }

    Teacher saveTeacher1() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("teacher1");
        teacher.setLastName("Bak");
        teacher.setEmail("teacher1@interia.pl");
        teacher.setSubjectName(Set.of("Fizyka", "Biologia"));
        return teacher;

    }

    AddSchoolSubjects addSubject() {
        return new AddSchoolSubjects("teacher4@interia.pl", Set.of("Matematyka", "Chemia"));
    }

    Teacher saveForUpdateSubjects() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("teacher4");
        teacher.setLastName("Bak");
        teacher.setEmail("teacher4@interia.pl");
        teacher.setSubjectName(new HashSet<>());
        return teacher;
    }

}
