package org.example.teacher;

import java.util.Set;

public class Response {

    String json = """
            [
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
                        "firstName": "Student13",
                        "lastName": "Bak",
                        "email": "student13@interia.pl",
                        "course": "Medycyna"
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
                        "firstName": "Student19",
                        "lastName": "Bak",
                        "email": "student19@interia.pl",
                        "course": "Humanistyczny"
                    },
                    "grades": []
                },
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
                        "firstName": "Student10",
                        "lastName": "Bak",
                        "email": "student10@interia.pl",
                        "course": "Matematyczno-Geograficzny"
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
                }
            ]""";
    Teacher saveTeacher(){
        Teacher teacher = new Teacher();
        teacher.setFirstName("teacher4");
        teacher.setLastName("Bak");
        teacher.setEmail("teacher4@interia.pl");
        teacher.setSubjectName(Set.of("Język Angielski Medyczny","Język Angielski"));
        return teacher;
    }
/*[
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
            "firstName": "Student13",
            "lastName": "Bak",
            "email": "student13@interia.pl",
            "course": "Medycyna"
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
            "firstName": "Student19",
            "lastName": "Bak",
            "email": "student19@interia.pl",
            "course": "Humanistyczny"
        },
        "grades": []
    },
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
            "firstName": "Student10",
            "lastName": "Bak",
            "email": "student10@interia.pl",
            "course": "Matematyczno-Geograficzny"
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
            "firstName": "Student6",
            "lastName": "Bak",
            "email": "student6@interia.pl",
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
            "firstName": "Student17",
            "lastName": "Bak",
            "email": "student17@interia.pl",
            "course": "Humanistyczny"
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
            "firstName": "Student",
            "lastName": "Bak",
            "email": "student@interia.pl",
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
    }
]




----------------------------------------------------------------------------------------------------
String json = "[\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student14\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student14@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student11\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student11@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": [\n" +
"            {\n" +
"                \"subject\": \"Anatomia \",\n" +
"                \"grades\": [\n" +
"                    \"  5 odpowiedz  2024-11-12\",\n" +
"                    \"  5 sprawdzian  2024-11-12\"\n" +
"                ]\n" +
"            }\n" +
"        ]\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student12\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student12@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": [\n" +
"            {\n" +
"                \"subject\": \"Anatomia \",\n" +
"                \"grades\": [\n" +
"                    \"  5 sprawdzian  2024-11-12\",\n" +
"                    \"  5 sprawdzian  2024-11-12\"\n" +
"                ]\n" +
"            }\n" +
"        ]\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student13\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student13@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student15\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student15@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student18\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student18@interia.pl\",\n" +
"            \"course\": \"Humanistyczny\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student19\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student19@interia.pl\",\n" +
"            \"course\": \"Humanistyczny\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student5\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student5@interia.pl\",\n" +
"            \"course\": \"Matematyczno-Geograficzny\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student10\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student10@interia.pl\",\n" +
"            \"course\": \"Matematyczno-Geograficzny\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    },\n" +
// Kontynuacja...
"    {\n" +
"        \"student\": {\n" +
"            \"firstName\": \"Student16\",\n" +
"            \"lastName\": \"Bak\",\n" +
"            \"email\": \"student16@interia.pl\",\n" +
"            \"course\": \"Medycyna\"\n" +
"        },\n" +
"        \"grades\": []\n" +
"    }\n" +
"]";







 */
}
