package org.example.reactiveuniversity.appconfig;


import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;

@Configuration
@EnableWebFluxSecurity

public class AppConfig {


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


        // return http.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.POST, "/aaa").permitAll().anyExchange().authenticated()).addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable).build();
        return http.authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/user/registration").hasRole("Office").pathMatchers(HttpMethod.POST, "/login").permitAll().pathMatchers(HttpMethod.GET, "/user/role").hasRole("Office")).addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }


}
