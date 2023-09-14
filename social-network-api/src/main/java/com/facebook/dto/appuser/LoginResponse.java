package com.facebook.dto.appuser;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private Long id;
    private Boolean status;
    private String error;
    private String token;
    private String[] role;

    public static LoginResponse Ok(long id, String token, String[] role) {
        return new LoginResponse(id, true, null, token, role);

    }

    public static LoginResponse Error(String message) {
        return new LoginResponse(null, false, message, null, null);

    }

}
