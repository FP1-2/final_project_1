package com.facebook.dto.app_user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserResponse {

    private Long id;

    private LocalDateTime created_date;

    private LocalDateTime last_modified_date;

    private String name;

    private String surname;

    private String username;

    private String email;

    private String address;

    private String avatar;

    private String headerPhoto;

    private Integer dateOfBirth;

}