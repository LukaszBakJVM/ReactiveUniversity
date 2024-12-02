package org.example.office;

import org.example.office.dto.WriteNewPersonOffice;

public class Response {
    String json = """

            {
                 "firstName": "firstName",
                    "lastName": "lastName",
                    "email": "email@email.com"
                    }
            """;


     WriteNewPersonOffice writeNewPersonOffice() {
        return new WriteNewPersonOffice("firstName", "lastName", "email@email.com");
    }
}
