package com.facebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Launcher {

    /**
     * @param args Command Line Arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

}











