package com.facebook.config.web;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
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
        ModelMapper mapper = new ModelMapper();

        Converter<Timestamp, LocalDateTime> timestampToLocalDateTime = new AbstractConverter<>() {
            @Override
            protected LocalDateTime convert(Timestamp source) {
                return source != null ? source.toLocalDateTime() : null;
            }
        };
        mapper.addConverter(timestampToLocalDateTime);

        return mapper;
    }
}

