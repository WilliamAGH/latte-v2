package com.williamcallahan.tui4j.springexample;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Example program for application configuration.
 * tui4j: examples/spring/src/main/java/com/williamcallahan/tui4j/springexample/ApplicationConfiguration.java
 */
@Configuration
public class ApplicationConfiguration {

    @Bean
    public Faker faker() {
        return new Faker();
    }
}
