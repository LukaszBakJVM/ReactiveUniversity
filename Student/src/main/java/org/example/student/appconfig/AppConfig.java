package org.example.student.appconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;


@Configuration
@EnableWebFluxSecurity
@EnableR2dbcAuditing
public class AppConfig {
    @Value("${teacherUrl}")
    private String baseUrl;


    @Bean
    public ApplicationRunner initializeDatabase(DatabaseClient databaseClient) {
        return sql -> {
            Resource resource = new ClassPathResource("schema.sql");
            String schemaSql;
            schemaSql = new String(Files.readAllBytes(resource.getFile().toPath()));
            databaseClient.sql(schemaSql).then().subscribe();
        };
    }


    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager, ServerAuthenticationConverter authenticationConverter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        http.authorizeExchange(requests -> requests.pathMatchers(HttpMethod.POST, "/student").hasRole("Office").pathMatchers(HttpMethod.POST, "/student/update").hasAnyRole("Office").pathMatchers(HttpMethod.GET, "/student/write-grades/").hasAnyRole("Office", "Teacher").pathMatchers(HttpMethod.GET, "/grades/my-grades").hasRole("Student").anyExchange().permitAll());

        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable);
        return http.build();
    }


    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().baseUrl(baseUrl);
    }


}
