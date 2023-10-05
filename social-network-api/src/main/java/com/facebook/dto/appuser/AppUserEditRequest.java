package com.facebook.dto.appuser;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AppUserEditRequest {

    private static final String NOT_EMPTY = "The field must not be empty.";

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The name must contain at least 2 characters")
    private String name;

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The surname must contain at least 2 characters")
    private String surname;

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The username must contain at least 2 characters")
    private String username;

    @NotNull(message = NOT_EMPTY)
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Incorrect email format")
    private String email;

    String address;

    String avatar;

    String headerPhoto;

    @Min(value = 6, message = "The user must be at least 6 years old")
    private Integer dateOfBirth;

}
