package com.facebook.dto.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserRequest extends AbstractAppUserRequest {

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

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%~*?&])[A-Za-z\\d@$!~%*?&]{7,}$",
            message = """
                    Password must contain at least 7 characters, one uppercase letter,
                    one lowercase letter, and one special character.
                    """)
    @NotNull(message = NOT_EMPTY)
    private String password;

}