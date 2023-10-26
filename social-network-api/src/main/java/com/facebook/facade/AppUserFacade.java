package com.facebook.facade;

import com.facebook.dto.appuser.*;
import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppUserFacade {

    private final ModelMapper modelMapper;
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

    public AppUserRequest convertToAppUserRequest(AppUser appUser) {
        return modelMapper.map(appUser, AppUserRequest.class);
    }

    public void updateToAppUser(AppUser existingAppUser, AppUserRequest appUserRequest) {
        existingAppUser.setName(appUserRequest.getName());
        existingAppUser.setEmail(appUserRequest.getEmail());
        //TODO інші поля для оновлення AppUser
    }

    public void updateAppUserFromEditRequest(AppUser appUser, AppUserEditRequest userEditReq) {
        Optional.ofNullable(userEditReq.getName()).ifPresent(appUser::setName);
        Optional.ofNullable(userEditReq.getSurname()).ifPresent(appUser::setSurname);
        Optional.ofNullable(userEditReq.getUsername()).ifPresent(appUser::setUsername);
        Optional.ofNullable(userEditReq.getEmail()).ifPresent(appUser::setEmail);
        Optional.ofNullable(userEditReq.getAddress()).ifPresent(appUser::setAddress);
        Optional.ofNullable(userEditReq.getAvatar()).ifPresent(appUser::setAvatar);
        Optional.ofNullable(userEditReq.getHeaderPhoto()).ifPresent(appUser::setHeaderPhoto);
        Optional.ofNullable(userEditReq.getDateOfBirth()).ifPresent(appUser::setDateOfBirth);
    }

    public AppUserChatResponse convertToAppUserChatResponse(AppUser appUser) {
        return modelMapper.map(appUser, AppUserChatResponse.class);
    }
}