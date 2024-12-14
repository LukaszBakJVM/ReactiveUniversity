package org.example.subject;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.subject.dto.SubjectDto;
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

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubjectApplicationTests {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    SubjectRepository subjectRepository;
    Response response = new Response();


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

    @BeforeEach
    void writeToDatabase() {
        subjectRepository.saveAll(response.saveSubjects).subscribe();


    }

    @AfterEach
    void clearDatabase() {
        subjectRepository.deleteAll().subscribe();
    }

    @Test
    void writeNewSubjects_shouldReturnCreated_whenUserIsAuthorized_OfficeRole() {
        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        SubjectDto body = response.subjectDto();
        webTestClient.post().uri("/subject").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange().expectStatus().isCreated();
    }

    @Test
    void writeNewSubjects_shouldReturnForbidden_whenUserIsAuthorized_TeacherRole() {
        String token = token("teacher1@interia.pl", "lukasz");
        SubjectDto body = response.subjectDto();

        webTestClient.post().uri("/subject").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange().expectStatus().isForbidden();

    }

    @Test
    void writeNewSubjects_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String token = token("student1@interia.pl", "lukasz");
        SubjectDto body = response.subjectDto();

        webTestClient.post().uri("/subject").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(body).exchange().expectStatus().isForbidden();
    }


    @Test
    void getAllSubjects_shouldReturnOk() {
        webTestClient.get().uri("/subject/all").exchange().expectStatus().isOk().expectBody().json(response.allSubjects);
    }


    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();
    }

}
