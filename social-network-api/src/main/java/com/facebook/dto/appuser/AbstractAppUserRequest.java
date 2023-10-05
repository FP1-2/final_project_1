package com.facebook.dto.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public abstract class AbstractAppUserRequest {

    private static final String NOT_EMPTY = "The field must not be empty.";

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The name must contain at least 2 characters")
    protected String name;

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The surname must contain at least 2 characters")
    protected String surname;

    @NotNull(message = NOT_EMPTY)
    @Size(min = 2, message = "The username must contain at least 2 characters")
    protected String username;

    @NotNull(message = NOT_EMPTY)
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Incorrect email format")
    protected String email;

    protected String address;
    protected String avatar;
    protected String headerPhoto;

    @Min(value = 6, message = "The user must be at least 6 years old")
    protected Integer dateOfBirth;
}
