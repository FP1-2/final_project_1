package com.facebook.config.generator;

import com.facebook.utils.Gen;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {

    private final ApplicationContext context;

    @Override
    public void run(String... args){
        System.out.println("Data generation is starting...");
        Gen gen = Gen.of(context);
        gen.genAppUser();
    }
}
