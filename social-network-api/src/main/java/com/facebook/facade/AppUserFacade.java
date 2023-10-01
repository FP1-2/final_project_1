package com.facebook.facade;

import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.GenAppUser;
import com.facebook.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserFacade {

    private final ModelMapper modelMapper;

    public AppUserResponse convertToAppUserResponse(AppUser appUser) {
        return modelMapper.map(appUser, AppUserResponse.class);
    }

    public AppUser convertToAppUser(GenAppUser dto) {
        return modelMapper.map(dto, AppUser.class);
    }

    public AppUser convertToAppUser(AppUserRequest appUserRequest) {
        return modelMapper.map(appUserRequest, AppUser.class);
    }

}