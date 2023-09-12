package com.facebook.config.web;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Використовується в пакеті `facade` для перетворення
 * даних між шарами, (DTO) і шаром доменних моделей.
 */
@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
