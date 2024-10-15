package org.example.course;

import org.example.course.dto.CourseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CourseApplicationTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeAll
    static void startPostgres() {
        postgres.start();

    }

    @AfterAll
    static void stopPostgres() {
        postgres.stop();
    }

    @Test
    @WithMockUser(roles = {"Office"})
    void createCourse_shouldReturnCreated_whenUserIsAuthorized() {
        webTestClient // Mock a bearer token
                .post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isCreated();


    }


    @Test
    void createCourse_shouldReturnForbidden() {
        webTestClient.post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isForbidden();
    }

    CourseDto courseDto() {
        Set<String> subjects = Set.of("Historia", "Matematyka");
        return new CourseDto("Ścisły", subjects);
    }

}
