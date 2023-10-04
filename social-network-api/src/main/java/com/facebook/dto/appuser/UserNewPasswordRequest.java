package com.facebook.dto.appuser;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserNewPasswordRequest {
    @NotNull
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%~*?&])[A-Za-z\\d@$!~%*?&]{7,}$",
            message = """
                    Password must contain at least 7 characters, one uppercase letter,
                    one lowercase letter, and one special character.
                    """)
    @NotNull
    private String newPassword;
}