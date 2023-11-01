package com.facebook.config.generator;

import com.facebook.utils.Gen;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataGenerator implements CommandLineRunner {

    public final ApplicationContext context;

    @Override
    public void run(String... args){
        log.info("Data generation is starting...");
        Gen.of(context);
        log.info("Data generation is finish");
    }

}
