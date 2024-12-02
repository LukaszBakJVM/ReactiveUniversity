package org.example.office;

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
class OfficeApplicationTests {


    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    OfficeRepository officeRepository;
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
    void createPersonOffice_shouldReturnCreated_whenUserIsAuthorized_OfficeRole() {
        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.post().uri("/office").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(response.writeNewPersonOffice()).exchange().expectStatus().isCreated().expectBody().json(response.json);

        Mono<Office> byEmail = officeRepository.findByEmail(response.writeNewPersonOffice().email());
        StepVerifier.create(byEmail).expectNextMatches(office -> office.getFirstName().equals("firstName") && office.getLastName().equals("lastName") && office.getEmail().equals("email@email.com")).verifyComplete();


    }

    @Test
    void createPersonOffice_shouldReturnForbidden_whenUserIsAuthorized_TeacherRole() {
        String token = token("teacher4@interia.pl", "lukasz");

        webTestClient.post().uri("/office").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(response.writeNewPersonOffice()).exchange().expectStatus().isForbidden();
    }

    @Test
    void createPersonOffice_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String token = token("student1@interia.pl", "lukasz");

        webTestClient.post().uri("/office").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(response.writeNewPersonOffice()).exchange().expectStatus().isForbidden();
    }


    private String token(String username, String password) {

        String authBase = String.format("http://localhost:%s/login", wireMockServer.getPort());

        Login login = new Login(username, password);
        return webTestClient.post().uri(authBase).bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }

}
