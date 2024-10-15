package org.example.course;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CourseApplicationTests {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:latest"
    ).withInitScript("schema.sql");
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    WebTestClient webTestClient;
    @BeforeAll
   static void  startPostgres(){
        postgres.start();

    }
    @AfterAll
    static void stopPostgres(){
        postgres.stop();
    }



    @Test
    void contextLoads() {
    }

}
