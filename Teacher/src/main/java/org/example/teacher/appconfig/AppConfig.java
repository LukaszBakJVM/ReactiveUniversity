package org.example.teacher.appconfig;

import org.example.teacher.security.BearerTokenFilter;
import org.example.teacher.security.JwtService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.nio.file.Files;

@Configuration
public class AppConfig {
    private final JwtService jwtService;

    public AppConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

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
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {


        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(jwtService);


        http.authorizeHttpRequests(requests -> requests.requestMatchers(mvc.pattern(HttpMethod.POST, "/teacher")).hasRole("office").requestMatchers(mvc.pattern(HttpMethod.POST, "/teacher/update")).hasAnyRole("Teacher", "Office").requestMatchers(mvc.pattern(HttpMethod.GET, "/teacher/{email}/name")).hasAnyRole("Teacher", "Office").anyRequest().authenticated());
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(bearerTokenFilter, AuthorizationFilter.class);
        return http.build();
    }
    @Bean
    public WebClient.Builder webclient() {
        return WebClient.builder();
    }


}
