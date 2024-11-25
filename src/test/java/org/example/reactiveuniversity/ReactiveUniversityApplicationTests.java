package org.example.reactiveuniversity;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveUniversityApplicationTests {


    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest").withInitScript("schema.sql");
    @LocalServerPort
    private static int dynamicPort;
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance().options(wireMockConfig().port(dynamicPort)).build();
    @Autowired
    WebTestClient webTestClient;
    @Autowired
    RegistrationRepository registrationRepository;
    Response response = new Response();


    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("office", wireMockServer::baseUrl);
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
        registrationRepository.deleteAll().subscribe();

    }

    @Test
    void createPersonOffice_shouldReturnCreated_whenUserIsAuthorizedOffice() {


        String token = token("lukasz.bak@interiowy.pl", "lukasz");
        RegistrationDto registration = registration("testoffice", "test2", "emailoffice@test", "password", "Office");

        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isCreated().expectBody().json(response.registrationDto);

        Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        StepVerifier.create(byEmail).expectNextCount(1).verifyComplete();

    }

    @Test
    void createPersonOffice_shouldThrowCustomValidationException_whenUserIsAuthorizedOffice() {

        String token = token("lukasz.bak@interiowy.pl", "lukasz");
        RegistrationDto registration = registration("firstNameOK", "lastNameOk", "emailemial.pl", "password", "Office");

        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isBadRequest().expectBody().json(response.validation);

        Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        StepVerifier.create(byEmail).expectNextCount(0).verifyComplete();

    }


    @Test
    void createPersonOffice_shouldThrowDuplicateEmailException_whenUserIsAuthorizedOffice() {


        String token = token("lukasz.bak@interiowy.pl", "lukasz");
        RegistrationDto registration = registration("firstNameOK", "lastNameOk", "email@emialOk.pl", "password", "Office");

        save().subscribe();


        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isEqualTo(HttpStatus.CONFLICT).expectBody().json(response.duplicateEmail);

        Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        StepVerifier.create(byEmail).expectNextCount(1).verifyComplete();

    }


    @Test
    void createPersonOffice_shouldReturnForbidden_whenUserIsAuthorizedStudent() {

        String token = token("student1@interia.pl", "lukasz");
        RegistrationDto registration = registration("firstName", "lastName", "email@emial.pl", "password", "Office");

        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isForbidden();


        Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        StepVerifier.create(byEmail).expectNextCount(0).verifyComplete();

    }

    @Test
    void createPersonOffice_shouldReturnForbidden_whenUserIsAuthorizedTeacher() {
        String token = token("teacher1@interia.pl", "lukasz");
        RegistrationDto registration = registration("firstName", "lastName", "email@emial.pl", "password", "Office");


        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isForbidden();


        Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        StepVerifier.create(byEmail).expectNextCount(0).verifyComplete();

    }

    private Mono<Registration> save() {
        return registrationRepository.save(response.save());
    }

    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());


        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }

    private RegistrationDto registration(String firstName, String lastName, String email, String password, String role) {
        return new RegistrationDto(firstName, lastName, email, password, role);
    }

}
