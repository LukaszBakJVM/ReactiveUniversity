package org.example.teacher;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherApplicationTests {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    TeacherRepository teacherRepository;
    Response response = new Response();


    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("token.store.path", () -> Paths.get(System.getProperty("user.dir"), "tokenstore.txt").toString());
        registry.add("student", wireMockServer::baseUrl);
        registry.add("course", wireMockServer::baseUrl);
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
    void clearDatabase() {
        teacherRepository.deleteAll().subscribe();

    }

    @Test
    void findMyStudents_shouldReturnOk_whenUserIsAuthorized_teacherRole() {
        String email = "teacher4@interia.pl";

        teacherRepository.save(response.saveTeacherForMyStudents()).subscribe();

        String token = token(email, "lukasz");

        webTestClient.get().uri("teacher/my-students").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.findMyStudents);


    }
    @Test
    void  writeSubjectsToTeacher_shouldReturnOk_whenUserIsAuthorized_OfficeRole(){

        String email = "teacher4@interia.pl";

        teacherRepository.save(response.saveForUpdateSubjects()).subscribe();

        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.put().uri("/teacher/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addSubject()).exchange().expectStatus().isNoContent();

        Mono<Teacher> byEmail = teacherRepository.findByEmail(email);

        StepVerifier.create(byEmail).expectNextMatches( t->t.getSubjectName().contains("Matematyka") && t.getSubjectName().contains("Chemia")).verifyComplete();

    }


    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }
}
