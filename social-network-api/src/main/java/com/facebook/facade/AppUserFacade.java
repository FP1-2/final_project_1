package com.facebook.facade;

import com.facebook.dto.appuser.AppUserRequest;
import com.facebook.dto.appuser.AppUserResponse;
import com.facebook.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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

    public AppUserRequest convertToCustomerRequest(AppUser customer) {
        return modelMapper.map(customer, AppUserRequest.class);
    }

    public AppUser convertToAppUser(AppUserRequest customerRequest) {
        return modelMapper.map(customerRequest, AppUser.class);
    }

    public void updateToAppUser(AppUser existingAppUser, AppUserRequest appUserRequest) {
        existingAppUser.setName(appUserRequest.getName());
        existingAppUser.setEmail(appUserRequest.getEmail());
        //TODO інші поля для оновлення AppUser
    }

}