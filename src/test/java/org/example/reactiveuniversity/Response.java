package org.example.reactiveuniversity;

public class Response {
    String registrationDto = "{\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"email\":\"email@emial.pl\",\"role\":\"Teacher\"}";


    Registration save() {
        Registration registration = new Registration();
        registration.setFirstName("firstNameOK");
        registration.setLastName("lastNameOk");
        registration.setEmail("email@emialOk.pl");
        registration.setPassword("password");
        registration.setRole("Office");
        return registration;
    }

    String connectionError = "{\"status\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Connection Error\"}";

    String duplicateEmail = "{\"status\":\"CONFLICT\",\"message\":\"Email email@emialOk.pl already exists\"}";
    String validation = "{\"status\":\"BAD_REQUEST\",\"message\":\"Enter valid email\"}";


}
