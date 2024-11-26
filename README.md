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

- **[Swagger UI - API Documentation (Registration) ](http://mojeip.dynu.net:8090/webjars/swagger-ui/index.html)**



 - login endpoint
  ```bash
  curl -X POST mojeip.dynu.net:8090/login -H "Content-Type: application/json" -d "{\"email\": \"lukasz.bak@interiowy.pl\", \"password\": \"lukasz\"}"
  ````

    available roles -> only user with role office allowed access
    
  ```bash
 curl -X GET mojeip.dynu.net:8090/user/role -H "Authorization: Bearer <token>"
 ````

- registration controler ->  register  new person (role office,teacher,student)  -> only user with role office allowed access
```bash
 curl -X POST mojeip.dynu.net:8090/user/registration -H "Content-Type: application/json" -H "Authorization: Bearer <token> " -d "{
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
############################################################################################################################

- **[Swagger UI - API Documentation](http://mojeip.dynu.net:8081/webjars/swagger-ui/index.html)**

- /teacher/update
- Assign a subject to a teacher  -> only person with office role
```bash
curl -X PUT "mojeip.dynu.net:8081/teacher/update"  -H "Authorization: Bearer <token>"  -H "Content-Type: application/json"  -d "{\"email\": \"teacher email\", \"subjects\": [\"subject\", \"subject1\"]}"
````

 -teacher post -> look registration controler

 -/teacher/private/{email} -> only person with office,teacher role

 ```bash
 curl -X GET mojeip.dynu.net:8081/teacher/private/teacher1@interia.pl -H "Authorization: Bearer <token>"
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
 











- **[Swagger UI - API Documentation](http://51.79.251.86:8082/webjars/swagger-ui/index.html)**

- **[Swagger UI - API Documentation](http://51.79.251.86:8083/webjars/swagger-ui/index.html)**

- **[Swagger UI - API Documentation](http://51.79.251.86:8084/webjars/swagger-ui/index.html)**

 - **[Swagger UI - API Documentation](http://51.79.251.86:8085/webjars/swagger-ui/index.html)**
