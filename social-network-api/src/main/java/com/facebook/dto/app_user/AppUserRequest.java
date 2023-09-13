package com.facebook.dto.app_user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class AppUserRequest {
    @NotNull
    @Size(min = 2, message = "The name must contain at least 2 characters")
    private String name;
    @NotNull
    @Size(min = 2, message = "The surname must contain at least 2 characters")
    private String surname;
    @NotNull
    @Size(min = 2, message = "The username must contain at least 2 characters")
    private String username;

    @NotNull
    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Incorrect email format")
    private String email;

    String address;

    String avatar;

    String headerPhoto;

    @Min(value = 6, message = "The user must be at least 6 years old")
    private Integer dateOfBirth;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%~*?&])[A-Za-z\\d@$!~%*?&]{7,}$",
            message = """
            Password must contain at least 7 characters, one uppercase letter,
            one lowercase letter, and one special character
            """)

    @NotNull
    private String password;
}