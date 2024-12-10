package org.example.teacher;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
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
    void writeToDatabase() {
        teacherRepository.saveAll(response.saveTeachers).subscribe();

    }
    @AfterEach
    void clearDatabase(){
        teacherRepository.deleteAll().subscribe();
    }

    @Test
    void findMyStudents_shouldReturnOk_whenUserIsAuthorized_teacherRole() {
        String email = "teacher4@interia.pl";



        String token = token(email, "lukasz");

        webTestClient.get().uri("teacher/my-students").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.findMyStudents);

    }

    @Test
    void getTeacherPrivateInformation_shouldReturnOk_whenUserIsAuthorized_TeacherRole() {
        String email = "teacher4@interia.pl";

        String teacherEmail = "teacher1@interia.pl";

        String token = token(email, "lukasz");
        webTestClient.get().uri("/teacher/private/{email}", teacherEmail).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.teacherPrivateInfo);

    }


    @Test
    void findMyStudents_shouldReturnForbidden_whenUserIsAuthorized_teacherOffice() {
        String email = "lukasz.bak@interiowy.pl";

        String token = token(email, "lukasz");

        webTestClient.get().uri("teacher/my-students").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isForbidden();


    }

    @Test
    void getAllTeachersPrivateInformation_shouldReturnOk_whenUserIsAuthorized_TeacherRole() {

        String email = "teacher4@interia.pl";



        String token = token(email, "lukasz");

        webTestClient.get().uri("/teacher/private/all").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.allTeachers);
    }

    @Test
    void findMyStudents_shouldReturnForbidden_whenUserIsAuthorized_teacherStudent() {
        String email = "student1@interia.pl";


        String token = token(email, "lukasz");

        webTestClient.get().uri("teacher/my-students").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isForbidden();


    }

    @Test
    void writeSubjectsToTeacher_shouldReturnOk_whenUserIsAuthorized_OfficeRole() {




        String token = token("lukasz.bak@interiowy.pl", "lukasz");

        webTestClient.put().uri("/teacher/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addSubject()).exchange().expectStatus().isNoContent();

        Mono<Teacher> byEmail = teacherRepository.findByEmail("teacher4@interia.pl");

        StepVerifier.create(byEmail).expectNextMatches(t -> t.getSubjectName().contains("Matematyka") && t.getSubjectName().contains("Chemia")).verifyComplete();

    }

    @Test
    void writeSubjectsToTeacher_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {

        String email = "student1@interia.pl";



        String token = token(email, "lukasz");

        webTestClient.put().uri("/teacher/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addSubject()).exchange().expectStatus().isForbidden();

    }

    @Test
    void writeSubjectsToTeacher_shouldReturnForbidden_whenUserIsAuthorized_OfficeTeacher() {

        String email = "teacher4@interia.pl";



        String token = token(email, "lukasz");

        webTestClient.put().uri("/teacher/update").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).bodyValue(response.addSubject()).exchange().expectStatus().isForbidden();

    }

    @Test
    void getTeacherPrivateInformation_shouldReturnOk_whenUserIsAuthorized_OfficeRole() {
        String email = "lukasz.bak@interiowy.pl";

        String teacherEmail = "teacher1@interia.pl";

        String token = token(email, "lukasz");
        webTestClient.get().uri("/teacher/private/{email}", teacherEmail).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.teacherPrivateInfo);

    }

    @Test
    void getTeacherPrivateInformation_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String email = "student1@interia.pl";

        String teacherEmail = "teacher1@interia.pl";

        String token = token(email, "lukasz");
        webTestClient.get().uri("/teacher/private/{email}", teacherEmail).header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isForbidden();

    }

    @Test
    void getAllTeachersPrivateInformation_shouldReturnForbidden_whenUserIsAuthorized_StudentRole() {
        String email = "student1@interia.pl";
        String token = token(email, "lukasz");
        webTestClient.get().uri("/teacher/private/all").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isForbidden();
    }

    @Test
    void getAllTeachersPrivateInformation_shouldReturnOk_whenUserIsAuthorized_OfficeRole() {

        String email = "lukasz.bak@interiowy.pl";



        String token = token(email, "lukasz");

        webTestClient.get().uri("/teacher/private/all").header("Authorization", "Bearer " + token).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.allTeachers);


    }

    @Test
    void getTeacherBySubject_shouldReturnOk() {

        String subject = "Fizyka";

        webTestClient.get().uri("/teacher/info/{subject}", subject).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody().json(response.infoSubject);
    }


    private String token(String email, String password) {

        String authBase = String.format("http://localhost:%s", wireMockServer.getPort());

        Login login = new Login(email, password);
        return webTestClient.post().uri(authBase + "/login").bodyValue(login).exchange().returnResult(String.class).getResponseBody().blockFirst();


    }
}
