package com.facebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class Launcher {
    private Launcher() {
        throw new UnsupportedOperationException("This main entry point");
    }

    /**
     * @param args Command Line Arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

}











