package org.example.reactiveuniversity;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.example.reactiveuniversity.dto.RegistrationDto;
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
        registry.add("baseUrl", wireMockServer::baseUrl);
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort() + "/" + postgreSQLContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", postgreSQLContainer::getUsername);
        registry.add("spring.r2dbc.password", postgreSQLContainer::getPassword);

    }
    //  @BeforeEach
    // void clearDatabase(){
    //     registrationRepository.deleteAll();
    // }

    @BeforeAll
    static void startPostgres() {
        postgreSQLContainer.start();

    }

    @AfterAll
    static void stopPostgres() {
        postgreSQLContainer.stop();
    }

    @Test
    void createPersonOffice_shouldReturnForbidden_whenUserIsAuthorized_OfficeTeacher() {
        String token = token("teacher1@interia.pl", "lukasz");
        RegistrationDto registration = registration("firstName", "lastName", "email@emial.pl", "password", "Office");


        webTestClient.post().uri("/user/registration").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).bodyValue(registration).exchange().expectStatus().isForbidden();
        //.isCreated().expectBody().json(response.registration);


        //   Mono<Registration> byEmail = registrationRepository.findByEmail(registration.email());
        //  StepVerifier.create(byEmail).expectNextMatches(office -> office.getFirstName().equals("firstName") && office.getLastName().equals("lastName") && office.getEmail().equals("email@email.com")).verifyComplete();


    }

    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s/login", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase).bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }

    private RegistrationDto registration(String firstName, String lastName, String email, String password, String role) {
        return new RegistrationDto(firstName, lastName, email, password, role);
    }

}
