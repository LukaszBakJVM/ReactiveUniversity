package org.example.course;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.course.dto.CourseDto;
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

import java.util.Objects;
import java.util.Set;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseApplicationTests {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    CourseRepository courseRepository;

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
    void createCourse_shouldReturnCreated_whenUserIsAuthorized_OfficeRole() {
        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.post().uri("/course").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isCreated();


    }

    @Test
    void createCourse_shouldReturnForbidden_whenUserIsAuthorized_TeacherRole() {
        String token = token("teacher@interiowy.pl", "lukasz");

        webTestClient.post().uri("/course").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isForbidden();


    }

    @Test
    void createCourse_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String token = token("student@interiowy.pl", "lukasz");

        webTestClient.post().uri("/course").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isForbidden();


    }


    @Test
    void createCourse_shouldReturnForbidden_whenUserIsNotAuthorized() {
        webTestClient.post().uri("/course").contentType(MediaType.APPLICATION_JSON).bodyValue(courseDto()).exchange().expectStatus().isForbidden();
    }

    @Test
    void deleteCourse_shouldReturnNoContent_whenUserIsAuthorized_OfficeRole() {

        String courseName = "delete";
        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.delete().uri("/course/{courseName}", courseName).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isNoContent();

        Objects.equals(webTestClient.get().uri("course/{course}", courseName).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(String.class), "{}");

    }

    @Test
    void deleteCourse_shouldReturnForbidden_whenUserIsAuthorized_TeacherRole() {

        String courseName = "delete";
        String token = token("teacher@interiowy.pl", "lukasz");

        webTestClient.delete().uri("/course/{courseName}", courseName).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isForbidden();


    }

    @Test
    void findCourseBySubject_shouldReturn_course1() {
        String subject = "subject2";
        webTestClient.get().uri("/course/{subject}/name", subject).accept(MediaType.APPLICATION_JSON).exchange().expectBody().json(response.courseBySubject);
    }

    @Test
    void findCourseByCourseName_shouldReturn_course2() {
        String courseName = "course2";
        webTestClient.get().uri("/course/{courseName}", courseName).accept(MediaType.APPLICATION_JSON).exchange().expectBody().json(response.course);

    }

    private CourseDto courseDto() {
        Set<String> subjects = Set.of("Historia", "Matematyka");
        return new CourseDto("Ścisły", subjects);
    }


    private String token(String username, String password) {

        String authBase = String.format("http://localhost:%s/auth", wireMockServer.getPort());

        Login login = new Login(username, password);
        String responseBody = webTestClient.post().uri(authBase).bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();
        return responseBody.split(":", 2)[1].replace("\"", "");

    }


}
