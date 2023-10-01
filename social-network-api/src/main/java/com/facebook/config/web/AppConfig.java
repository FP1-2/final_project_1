package com.facebook.config.web;

import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

        Converter<Clob, String> clobToString = new AbstractConverter<>() {
            @Override
            protected String convert(Clob source) {
                return Optional.ofNullable(source).map(s -> {
                    try {
                        return s.getSubString(1, (int) s.length());
                    } catch (SQLException e) {
                        throw new RuntimeException("Failed to convert CLOB to string", e);
                    }
                }).orElse(null);
            }
        };

        Converter<Object, List<Long>> stringToList = new AbstractConverter<>() {

            /**
             * Перетворює рядкове представлення списку ID (розділених комами) у список Long.
             * <p>
             * Використовується для перетворення рядків,
             * що містять список ID "comments", "likes", "reposts",
             * отриманих з бази даних, у список об'єктів Long для подальшого використання у DTO.
             * </p>
             *
             * @param source Рядкове представлення списку ID.
             * @return Список ID як об'єкти Long.
             */
            @Override
            protected List<Long> convert(Object source) {
                return Optional.ofNullable(source)
                        .map(Object::toString)
                        .filter(input -> !input.isEmpty())
                        .map(input -> Arrays.stream(input.split(","))
                                .map(Long::valueOf)
                                .toList())
                        .orElse(new ArrayList<>());
            }
        };

        mapper.addConverter(timestampToLocalDateTime);
        mapper.addConverter(clobToString);
        mapper.addConverter(stringToList);

        return mapper;
    }
}

