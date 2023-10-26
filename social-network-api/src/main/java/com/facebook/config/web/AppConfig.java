package com.facebook.config.web;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
            /**
             * Перетворює Timestamp у LocalDateTime.
             *
             * @param source Об'єкт Timestamp.
             * @return Об'єкт LocalDateTime.
             */
            @Override
            protected LocalDateTime convert(Timestamp source) {
                return source != null ? source.toLocalDateTime() : null;
            }
        };
        mapper.addConverter(timestampToLocalDateTime);
        return mapper;
    }
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}

