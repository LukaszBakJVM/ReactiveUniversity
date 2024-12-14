package org.example.subject;

import org.example.subject.dto.SubjectDto;

import java.util.List;

public class Response {
    String allSubjects = """
            [{"subject":"Biologia"},{"subject":"Fizyka"},{"subject":"Matematyka"},{"subject":"Chemia"}]
            """;

    SubjectDto subjectDto (){
        return new SubjectDto("Geografia");
    }


    List<Subject>saveSubjects = List.of(save(),save1(),save2(),save3());

    private Subject save(){
     return   new Subject("Chemia");
    }
    private Subject save1(){
        return   new Subject("Fizyka");
    }
    private Subject save2(){
        return new Subject("Matematyka");
    }
    private Subject save3(){
        return   new Subject("Biologia");
    }


}
