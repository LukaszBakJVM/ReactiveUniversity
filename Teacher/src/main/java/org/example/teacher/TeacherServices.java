package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.TeacherInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TeacherServices {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;


    public TeacherServices(TeacherMapper teacherMapper, TeacherRepository teacherRepository) {
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;

    }

    Mono<WriteNewTeacherDto> createTeacher(WriteNewTeacherDto dto) {
        return teacherRepository.save(teacherMapper.dtoToEntity(dto)).map(teacherMapper::entityToDto);


    }

    Mono<AddSchoolSubjects> addSchoolSubjectsMono(List<String> schoolSubjectsMono, String email) {


        return teacherRepository.findByEmail(email).flatMap(teacher -> {
            teacher.setId(teacher.getId());
            teacher.getSubjectName().addAll(schoolSubjectsMono);
            return teacherRepository.save(teacher);
        }).map(teacherMapper::addSchoolSubjects);

    }

    Mono<TeacherInfo> findTeacher(String email) {
        return teacherRepository.findByEmail(email).map(teacherMapper::teacherInfo);
    }

    Flux<TeacherInfo> allTeacherInfo() {
        return teacherRepository.findAll().map(teacherMapper::teacherInfo);
    }


}


