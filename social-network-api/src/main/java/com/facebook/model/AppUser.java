package com.facebook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    //Використовується дата генератором
    public AppUser(String name, String surname, String username, String email, String address, String avatar, String headerPhoto, Integer dateOfBirth) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.address = address;
        this.avatar = avatar;
        this.headerPhoto = headerPhoto;
        this.dateOfBirth = dateOfBirth;
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
