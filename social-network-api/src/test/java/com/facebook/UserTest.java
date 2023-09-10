package com.facebook;

import com.facebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {

    @Autowired
    private TestEntityManager tem;

    @Test
    public void dateSetting() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setPassword("secret");

        User savedUser = tem.persistAndFlush(user);

        assertThat(savedUser.getCreatedDate()).isNotNull();
        assertThat(savedUser.getLastModifiedDate()).isNotNull();
    }

    @Test
    public void getSetRoles() {
        User user = new User();
        String[] roles = {"ROLE_ADMIN", "ROLE_USER"};

        user.setRoles(roles);

        String[] retrievedRoles = user.getRoles();
        assertThat(retrievedRoles).containsExactly("ROLE_ADMIN", "ROLE_USER");

        assertThat(user.getRoles()).containsExactly("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    public void dateModification() {
        User user = new User();
        user.setName("John");
        user.setSurname("Doe");
        user.setPassword("secret");

        User savedUser = tem.persistAndFlush(user);

        LocalDateTime initialLastModifiedDate = savedUser.getLastModifiedDate();

        savedUser.setName("Johnathan");
        tem.persistAndFlush(savedUser);

        assertThat(savedUser.getLastModifiedDate()).isNotEqualTo(initialLastModifiedDate);
        assertThat(savedUser.getLastModifiedDate()).isAfter(initialLastModifiedDate);
    }
}
