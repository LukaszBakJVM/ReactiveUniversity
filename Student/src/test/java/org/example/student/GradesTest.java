package org.example.student;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.student.grades.Grades;
import org.example.student.grades.GradesRepository;
import org.example.student.grades.dto.GradesRequest;
import org.junit.jupiter.api.*;
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

import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GradesTest {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema-grades.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    GradesRepository gradesRepository;

    Response response = new Response();

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("token.store.path", () -> Paths.get(System.getProperty("user.dir"), "tokenstore.txt").toString());
        registry.add("teacherUrl", wireMockServer::baseUrl);
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

    @BeforeEach
    void writeToDatabase() {
        studentRepository.saveAll(response.saveStudents()).subscribe();
        gradesRepository.saveAll(response.saveGrades).subscribe();
    }

    @AfterEach
    void cleanDataBase() {
        studentRepository.deleteAll().subscribe();
        gradesRepository.deleteAll().subscribe();
    }

    @Test
    void writeGradesToStudent_shouldReturnCreated_whenUserIsAuthorized_TeacherRole() {

        String teacherEmail = "teacher4@interia.pl";
        GradesRequest gradesRequest = response.gradesRequest();
        String token = token(teacherEmail, "lukasz");

        webTestClient.post().uri("/grades").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(gradesRequest).exchange().expectStatus().isCreated();

        Mono<Grades> byEmailAndSubject = gradesRepository.findByEmailAndSubject(gradesRequest.email(), gradesRequest.subject());

        StepVerifier.create(byEmailAndSubject).expectNextMatches(g -> g.getGradesDescription().size() == 1 && g.getTeacher().equals("Teacher4 Bak")).verifyComplete();
    }

    @Test
    void findMyGrades_shouldReturnOk_whenUserIsAuthorized_StudentRole() {
        String token = token("student1@interia.pl", "lukasz");
        webTestClient.get().uri("/grades/my-grades").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.gradesResponseJson);

    }


    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }

}
