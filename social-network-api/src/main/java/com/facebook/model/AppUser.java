package com.facebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Клас, що представляє собою користувача в системі. Основні поля класу:
 * <ul>
 *     <li>{@link #name} - Ім'я користувача.</li>
 *     <li>{@link #surname} - Прізвище користувача.</li>
 *     <li>{@link #username} - Логін користувача.</li>
 *     <li>{@link #email} - Електронна пошта користувача.</li>
 *     <li>{@link #address} - Адреса користувача.</li>
 *     <li>{@link #avatar} - Аватар користувача.</li>
 *     <li>{@link #headerPhoto} - Зображення заголовка користувача.</li>
 *     <li>{@link #dateOfBirth} - Дата народження користувача.</li>
 *     <li>{@link #password} - Пароль користувача.</li>
 *     <li>{@link #roles} - Ролі користувача в системі.</li>
 * </ul>
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class AppUser extends AbstractEntity {

    private static final String DELIMITER = ":";

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    String address;

    String avatar;

    String headerPhoto;

    private Integer dateOfBirth;

    @Column(nullable = false)
    String password;

    private String roles;

    public void setRoles(String[] roles) {
        this.roles = String.join(DELIMITER, roles);
    }

    public String[] getRoles() {
        return this.roles.split(DELIMITER);
    }

    @Override
    public String toString() {
        return String.format("Customer{id=%d, Name=%s, Surname=%s, Username=%s, Email=%s, "
                        + "Address=%s, Avatar=%s, Header photo=%s, "
                        + "Age=%s, Password=%s, Roles=%s}",
                getId(), name, surname, username, email, address,
                avatar, headerPhoto, dateOfBirth,
                password, roles);
    }

}
