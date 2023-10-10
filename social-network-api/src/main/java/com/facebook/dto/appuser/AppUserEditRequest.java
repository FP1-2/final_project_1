package com.facebook.dto.appuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AppUserEditRequest extends AbstractAppUserRequest {

    @Size(min = 2, message = "The name must contain at least 2 characters")
    protected String name;

    @Size(min = 2, message = "The surname must contain at least 2 characters")
    protected String surname;

    @Size(min = 2, message = "The username must contain at least 2 characters")
    protected String username;

    @Email(regexp = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Incorrect email format")
    protected String email;

}
