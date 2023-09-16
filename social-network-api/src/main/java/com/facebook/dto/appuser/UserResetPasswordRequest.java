package com.facebook.dto.appuser;

import lombok.Data;

@Data
public class UserResetPasswordRequest {
    String email;
    String newPassword;
}