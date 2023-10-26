package com.facebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
    private Boolean isOnline;

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

//    @ManyToMany(mappedBy = "chatParticipants")
//    private Set<Chat> chats = new HashSet<>();



}
