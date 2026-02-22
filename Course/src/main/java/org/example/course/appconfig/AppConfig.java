package org.example.course.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class AppConfig {


  /*  @Bean
    public ApplicationRunner initializeDatabase(DatabaseClient databaseClient) {
        return sql -> {
            Resource resource = new ClassPathResource("schema.sql");
            String schemaSql;
            schemaSql = new String(Files.readAllBytes(resource.getFile().toPath()));
            databaseClient.sql(schemaSql).then().subscribe();
        };
    }*/

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {


        http.authorizeExchange(request -> request.pathMatchers(HttpMethod.POST, "/course").authenticated()
                        .pathMatchers(HttpMethod.DELETE, "/course/*").authenticated().anyExchange().permitAll()).
                oauth2ResourceServer(spec -> spec.jwt((jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))))
                .csrf(ServerHttpSecurity.CsrfSpec::disable).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable);

        return http.build();

    }

    public ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        return new ReactiveJwtAuthenticationConverterAdapter(jwt -> {
            Collection<GrantedAuthority> authorities = grantedAuthoritiesConverter.convert(jwt);


            Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess != null && realmAccess.containsKey("roles")) {

                Collection<String> roles = realmAccess.get("roles");

                List<SimpleGrantedAuthority> extraAuthorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();

                authorities.addAll(extraAuthorities);
            }

            return new JwtAuthenticationToken(jwt, authorities);
        });
    }


}
