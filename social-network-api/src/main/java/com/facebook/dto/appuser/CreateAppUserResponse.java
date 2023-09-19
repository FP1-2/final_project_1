package com.facebook.dto.appuser;

import com.facebook.controller.SignupResponse;

public record CreateAppUserResponse(String massage,
                                    String error) implements SignupResponse {
    public static CreateAppUserResponse ok(String massage) {
        return new CreateAppUserResponse(massage, null);
    }

    public static CreateAppUserResponse error(String massage, String error) {
        return new CreateAppUserResponse(massage, error);
    }

}
