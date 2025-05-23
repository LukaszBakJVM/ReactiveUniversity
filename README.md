School management program. A teacher can have many students and a student can have many teachers.
The teacher has the following fields: name, surname, email and subjects he teaches.
The student has the following fields: name, surname, email and field of study.

The login used was Bearer token. 
Based on the token, the teacher checks which students.

Based on the token, the student checks which teachers, grades and subjects.



When registering firstName ,lastName and email 
is sent to the appropriate microservice depending on the role.

- registration  login:password
- lukasz.bak@interiowy.pl:lukasz  ->  role office


[**__Swagger UI - API Documentation (Registration)__**](http://university.ddnsfree.com:8080/webjars/swagger-ui/index.html)







 - login endpoint
  ```bash
  curl -X POST https://university.ddnsfree.com/login -H "Content-Type: application/json" -d "{\"email\": \"lukasz.bak@interiowy.pl\", \"password\": \"lukasz\"}"
  ````

    available roles -> only user with role office allowed access
    
 ```bash
 curl -X GET https://university.ddnsfree.com/user/role -H "Authorization: Bearer <token>"
 ````

- registration controller ->  register  new person (role office,teacher,student)  -> only user with role office allowed access
```bash
 curl -X POST https://university.ddnsfree.com/user/registration -H "Content-Type: application/json" -H "Authorization: Bearer <token> " -d "{
    \"firstName\":\"John\",
    \"lastName\": \"Doe\",
    \"email\": \"johndoe@example.com\",
    \"password\": \"yourpassword\",
    \"role\": \"Teacher\"
  }"
````

  error
   - DuplicateEmailException ->  when you try to re-register email
   - CustomValidationException.java -> when  validation (email, firstName or lastName is smaller than 3 letter
   - ConnectionException.java  -> when   connection between microservices  is down.
     
- 
#############################################################################################

- **[Swagger UI - API Documentation (Teacher)](http://university.ddnsfree.com:8081/webjars/swagger-ui/index.html)**

               email : password
  teacher1@interia.pl:lukasz
  ......             :lukasz
  teacher9@interia.pl:lukasz

- /teacher/update
- Assign a subject to a teacher  -> only person with office role
```bash
curl -X PUT "https://university.ddnsfree.com/teacher/update"  -H "Authorization: Bearer <token>"  -H "Content-Type: application/json"  -d "{\"email\": \"teacher email\", \"subjects\": [\"subject\", \"subject1\"]}"
````

 -teacher post -> look registration controller

 -/teacher/private/{email} -> only person with office,teacher role
 private information about teacher 

 ```bash
 curl -X GET https://university.ddnsfree.com/teacher/private/teacher1@interia.pl -H "Authorization: Bearer <token>"
```

 response 

 ```json
{
    "firstName": "Teacher1",
    "lastName": "Bak",
    "email": "teacher1@interia.pl",
    "subjectName": [
        "Matematyka dla informatyków",
        "Podstatwy Linuxa"
    ]
}
````
-/teacher/private/all

 ```bash
 curl -X GET https://university.ddnsfree.com/teacher/private/all -H "Authorization: Bearer <token>"
```

 response 

```
[
    {
        "firstName": "Teacher",
        "lastName": "Bak",
        "email": "Teacher@interia.pl",
        "subjectName": []
    },
    {
        "firstName": "Teacher2",
        "lastName": "Bak",
        "email": "teacher2@interia.pl",
        "subjectName": [
            "Java",
            "C++",
            "Anatomia",
            "Chemia",
            "Biologia"
        ]
    },....]
```

Get /teacher/my-students    information about  what students the teacher has
information based on email from token
```bash
 curl -X GET https://university.ddnsfree.com/teacher/my-students -H "Authorization: Bearer <token>"
```

 response

```json
[
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
    },.....]
```

#########################################################################

- **[Swagger UI - API Documentation (Office)](http://university.ddnsfree.com:8082/webjars/swagger-ui/index.html)**

- look - registration controller
- 
#########################################################################


- **[Swagger UI - API Documentation (Subject)](http://university.ddnsfree.com:8083/webjars/swagger-ui/index.html)**

- Post /subject  -> only person with office role
- create new subject

 ```bash
 curl -X POST https://university.ddnsfree.com/subject  -H "Authorization: Bearer <token> " -d "{
    \"subject\":\"subjectName\" }"
  ````
Delete  /subject/{subjectName} -> only person with office role
delete subject
```bash 
curl -X DELETE "https://university.ddnsfree.com/subject/{subjectName}" -H "Content-Type: application/json" -H "Authorization: Bearer <token>"
````

-Get /subject/all  
find all subject
```bash
 curl -X GET https://university.ddnsfree.com/subject/all -H "Content-Type: application/json"
````
########################################################################

  - **[Swagger UI - API Documentation (Course)](http://university.ddnsfree.com:8084/webjars/swagger-ui/index.html)**

- Post /course  -> only person with office role
- create new course

 ```bash
curl -X POST "https://university.ddnsfree.com/course"  -H "Authorization: Bearer <token>" -d "{\"courseName\": \"Course Name\", \"subjects\": [\"subject1\", \"subject2\", \"subject3\"]}"

 ````
- Delete  /subject/{subjectName} -> only person with office role
- delete /course
```bash 
curl -X DELETE "https://university.ddnsfree.com/course/{course}" -H "Authorization: Bearer <token>"
````
- Get /course/all
- all courses
  ```bash
  curl -X GET "https://university.ddnsfree.com/course/all" -H "Content-Type: application/json"
  ```
  - get /course/{subject}/name
  - find courses containing  some subject
 ```bash
curl -X GET "mojeip.dynu.net:8084/course/{subject}/name" -H "Content-Type: application/json"
```
- get course/{course}
- show  what subjects are included in the course
 ```bash
curl -X GET "https://university.ddnsfree.com/course/{course}" -H "Content-Type: application/json"
```

- **[Swagger UI - API Documentation(Student)](http://university.ddnsfree.com:8085/webjars/swagger-ui/index.html)**
-  email : password
-  student@interia.pl:lukasz
  ......             :lukasz
-  student20@interia.pl:lukasz
- Post /student -> look registration controller
- 
-Put /student/update  -> only person with office role

write course to student
```
curl -X PUT "https://university.ddnsfree.com/student/update" -H "Content-Type: application/json" -H "Authorization: Bearer <token>" -d "{
    "course": "course name",
    "studentEmail": "student email"
}"
```
-
-Get /student/{email}
- get student information by email
```
curl -X GET "https://university.ddnsfree.com/student/{email}" -H "Content-Type: application/json"
```
-
- Get /student/assigned 
- get students with course
```
curl -X GET "https://university.ddnsfree.com/student/assigned" -H "Content-Type: application/json"
```

- Get /student/unassigned
- get students without course
``` 
curl -X GET "https://university.ddnsfree.com/student/unassigned" -H "Content-Type: application/json"
```
-
-Get /student/studentInfo/{course}
-get students by course
``` 
curl -X GET "https://university.ddnsfree.com/student/studentInfo/{course}" -H "Content-Type: application/json"
```
-
-Get /student/teachers
-get my teachers find teachers who teach me based on token
``` 
curl -X GET https://university.ddnsfree.com/student/teachers -H "Authorization: Bearer <token>"
```

-
-
Post  /grades  -> only person with Teacher role

write grades to student

``` 
curl -X POST "https://university.ddnsfree.com/grades" -H "Authorization: Bearer <token>" -d "{\"email\": \"student email\",  \"subject\": \"example subject\",\"gradesDescription\": \"  example description\"}"
```
-
Get /grades/my-grades

get my grades  based on token

``` 
curl -X GET https://university.ddnsfree.com/grades/my-grades -H "Authorization: Bearer <token>"
```



