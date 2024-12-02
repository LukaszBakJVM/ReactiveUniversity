package org.example.student;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentApplicationTests {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema-student.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    StudentRepository studentRepository;

    Response response = new Response();

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("baseUrl", wireMockServer::baseUrl);
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
    void writeCourseToStudent_shouldReturnNoContent_whenUserIsAuthorized_OfficeRole() {
        String token = token("lukasz.bak@interiowy.pl", "lukasz");
        studentRepository.saveAll(response.saveStudents()).subscribe();
        webTestClient.put().uri("/student/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addCourse()).exchange().expectStatus().isNoContent();

        Mono<Student> byEmail = studentRepository.findByEmail("student@email.com");

        StepVerifier.create(byEmail).expectNextMatches(s -> s.getCourse().equals("Medycyna")).verifyComplete();

    }
    @Test
    void writeCourseToStudent__shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String token = token("student1@interia.pl", "lukasz");
        webTestClient.put().uri("/student/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addCourse()).exchange().expectStatus().isForbidden();



    }
    @Test
    void writeCourseToStudent__shouldReturnForbiddent_whenUserIsAuthorized_TeacherRole() {
        String token = token("teacher4@interia.pl", "lukasz");
        webTestClient.put().uri("/student/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addCourse()).exchange().expectStatus().isForbidden();



    }

    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }


}
