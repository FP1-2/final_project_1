package com.facebook.dto.appuser;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserRequest extends AbstractAppUserRequest {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%~*?&])[A-Za-z\\d@$!~%*?&]{7,}$",
            message = """
                    Password must contain at least 7 characters, one uppercase letter,
                    one lowercase letter, and one special character.
                    """)
    @NotNull(message = "The field must not be empty.")
    private String password;
}