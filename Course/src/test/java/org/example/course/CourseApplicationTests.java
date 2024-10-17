package org.example.course;

import org.example.course.dto.CourseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseApplicationTests {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @Autowired
    WebTestClient webTestClient;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);
    }

    @BeforeAll
    static void startPostgres() {
        postgreSQLContainer.start();

    }

    @AfterAll
    static void stopPostgres() {
        postgreSQLContainer.stop();
    }


    @Test
    void createCourse_shouldReturnCreated_whenUserIsAuthorized() {
        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.post().uri("/course").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isCreated();


    }


    @Test
    void createCourse_shouldReturnForbidden() {
        webTestClient.post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isForbidden();
    }

    CourseDto courseDto() {
        Set<String> subjects = Set.of("Historia", "Matematyka");
        return new CourseDto("Ścisły", subjects);
    }

    private String token(String username, String password) {
        Login login = new Login(username, password);
        String responseBody = webTestClient.post().uri("http://localhost:8080/auth").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();
        return responseBody.split(":", 2)[1].replace("\"", "");

    }

}
