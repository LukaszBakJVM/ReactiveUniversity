package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.TeacherPrivateInfo;
import org.example.teacher.dto.TeacherPublicInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    Mono<AddSchoolSubjects> addSchoolSubjectsMono(AddSchoolSubjects schoolSubjectsMono, String email) {


        return teacherRepository.findByEmail(email).flatMap(teacher -> {
            teacher.setId(teacher.getId());
            teacher.getSubjectName().addAll(schoolSubjectsMono.subjects());
            return teacherRepository.save(teacher);
        }).map(teacherMapper::addSchoolSubjectsToDto);

    }

    Mono<TeacherPrivateInfo> findTeacher(String email) {
        return teacherRepository.findByEmail(email).map(teacherMapper::teacherPrivateInfo);
    }

    Flux<TeacherPrivateInfo> allTeacherInfo() {
        return teacherRepository.findAll().map(teacherMapper::teacherPrivateInfo);
    }

    Flux<TeacherPublicInfo> teacherPublicInfo(String subjectName) {
        return teacherRepository.findTeacherBySubjectNameContains(subjectName).map(teacherMapper::teacherPublicInfo);
    }


}


