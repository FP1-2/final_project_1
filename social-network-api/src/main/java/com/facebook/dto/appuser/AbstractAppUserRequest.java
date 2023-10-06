package com.facebook.dto.appuser;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public abstract class AbstractAppUserRequest {

    protected String address;

    protected String avatar;

    protected String headerPhoto;

    @Min(value = 6, message = "The user must be at least 6 years old")
    protected Integer dateOfBirth;

}
