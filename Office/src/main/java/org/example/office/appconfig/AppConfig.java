package org.example.office.appconfig;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableKafka
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

        http.authorizeExchange(requests -> requests.pathMatchers(HttpMethod.POST, "/office").hasRole("Office").pathMatchers(HttpMethod.GET, "/office/{email}").hasAnyRole("Office").pathMatchers("/webjars/**").permitAll().pathMatchers("/v3/api-docs/**").permitAll().anyExchange().authenticated());

        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable);
        return http.build();


    }
    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "your-consumer-group");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, WriteNewPersonOffice.class.getName());  // Określamy typ obiektu
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");  // Możesz dodać bardziej restrykcyjne pakiety
        return consumerProps;
    }

    @Bean
    public ConsumerFactory<String, WriteNewPersonOffice> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new JsonDeserializer<>(WriteNewPersonOffice.class))
        );
    }
}
