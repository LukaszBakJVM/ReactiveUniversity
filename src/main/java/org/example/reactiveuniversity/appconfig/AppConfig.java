package org.example.reactiveuniversity.appconfig;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableR2dbcAuditing
@EnableKafka

public class AppConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;


   // @Value("${spring.kafka.consumer.key-deserializer}")
   // private String keydeserializer;
   // @Value("${spring.kafka.consumer.value-deserializer}")
   // private String valuedeserializer;
   // @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
   // private String trustedPacked;

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


        http.authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/user/registration").hasRole("Office").pathMatchers(HttpMethod.POST, "/login").permitAll().pathMatchers(HttpMethod.GET, "/user/role").hasRole("Office").anyExchange().permitAll());
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION).httpBasic(ServerHttpSecurity.HttpBasicSpec::disable).formLogin(ServerHttpSecurity.FormLoginSpec::disable).csrf(ServerHttpSecurity.CsrfSpec::disable).cors(ServerHttpSecurity.CorsSpec::disable);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ReactiveAuditorAware<String> auditorProvider() {
        return () -> ReactiveSecurityContextHolder.getContext().map(securityContext -> securityContext.getAuthentication().getName());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplateObject() {

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "your-consumer-group");
       // consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, WriteNewPerson.class.getName());
        // consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,keydeserializer);
        // consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,valuedeserializer);
        // consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, trustedPacked);
        return consumerProps;
    }

    @Bean
    public ConsumerFactory<String, WriteNewPerson> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), new ErrorHandlingDeserializer<>(new JsonDeserializer<>(WriteNewPerson.class)));
    }


}
