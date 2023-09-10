package com.facebook;

import com.facebook.model.User;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager tem;

    @Test
    public void dateSetting() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("secret");

        User savedUser = tem.persistAndFlush(user);

        Stream.of(savedUser.getCreatedDate(), savedUser.getLastModifiedDate())
                .forEach(date -> assertThat(date).isNotNull());
    }

    @Test
    public void getSetRoles() {
        User user = new User();
        String[] roles = {"ROLE_ADMIN", "ROLE_USER"};

        user.setRoles(roles);

        String[] retrievedRoles = user.getRoles();
        Stream.of(retrievedRoles, user.getRoles())
                .forEach(roles0 -> assertThat(roles0)
                        .containsExactly("ROLE_ADMIN", "ROLE_USER"));

    }

    @Test
    public void dateModification() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("secret");

        User savedUser = tem.persistAndFlush(user);

        LocalDateTime initialLastModifiedDate = savedUser.getLastModifiedDate();

        savedUser.setName("Johnathan");
        tem.persistAndFlush(savedUser);

        assertThat(savedUser.getLastModifiedDate()).satisfies(date -> {
            assertThat(date).isNotEqualTo(initialLastModifiedDate);
            assertThat(date).isAfter(initialLastModifiedDate);
        });
    }

    @Test
    public void usernameNotNull() {
        User user = new User();
        user.setSurname("Doe");
        user.setPassword("secret");
        user.setEmail("test@example.com");

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(user));
    }

    @Test
    public void emailNotNull() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setPassword("secret");

        assertThrows(ConstraintViolationException.class,
                () -> tem.persistAndFlush(user));
    }
}
