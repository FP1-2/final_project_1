package com.facebook.dto.appuser;

import java.time.LocalDateTime;

import com.facebook.controller.auth.SignupResponse;
import lombok.Data;

@Data
public class AppUserResponse implements SignupResponse {

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