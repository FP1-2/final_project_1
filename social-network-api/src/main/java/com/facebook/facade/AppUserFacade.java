package com.facebook.facade;

import com.facebook.dto.app_user.AppUserRequest;
import com.facebook.dto.app_user.AppUserResponse;
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

    public void updateToCustomer(AppUser existingCustomer, AppUserRequest customerRequest) {
        existingCustomer.setName(customerRequest.getName());
        existingCustomer.setEmail(customerRequest.getEmail());
        //TODO інші поля для оновлення AppUser
    }
}