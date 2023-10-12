package com.facebook.facade;

import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.dto.appuser.GenAppUser;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserFacade {

    private final ModelMapper modelMapper;
    private final AppUserService service;
    public AppUserResponse convertToAppUserResponse(AppUser appUser) {
        AppUserResponse response = modelMapper.map(appUser, AppUserResponse.class);
        response.setCreated_date(appUser.getCreatedDate());
        response.setLast_modified_date(appUser.getLastModifiedDate());
        return response;
    }

    public AppUser convertToAppUser(GenAppUser dto) {
        return modelMapper.map(dto, AppUser.class);
    }

    public AppUser convertToAppUser(AppUserRequest appUserRequest) {
        return modelMapper.map(appUserRequest, AppUser.class);
    }
//    public AppUser convertToAppUser(String username){
//        return service.findByUsername(username);
//    }
}