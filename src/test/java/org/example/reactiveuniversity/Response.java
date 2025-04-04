package org.example.reactiveuniversity;

public class Response {
    String registrationOffice = "{\"firstName\":\"testoffice\",\"lastName\":\"test2\",\"email\":\"emailoffice@test\",\"role\":\"Office\"}";

    String registrationStudent = "{\"firstName\":\"testofstudent\",\"lastName\":\"test2\",\"email\":\"emailstudent@test\",\"role\":\"Student\"}";

    String registrationTeacher = "{\"firstName\":\"testteacher\",\"lastName\":\"test2\",\"email\":\"emailteacher@test\",\"role\":\"Teacher\"}";


    Registration save() {
        Registration registration = new Registration();
        registration.setFirstName("firstNameOK");
        registration.setLastName("lastNameOk");
        registration.setEmail("email@emialOk.pl");
        registration.setPassword("password");
        registration.setRole("Office");
        return registration;
    }

    String duplicateEmail = "{\"status\":\"CONFLICT\",\"message\":\"Email email@emialOk.pl already exists\"}";

    String validation = "{\"status\":\"BAD_REQUEST\",\"message\":\"Enter valid email\"}";

    String role = "[\"Office\", \"Teacher\", \"Student\"]";



}
