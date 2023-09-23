package com.facebook.dto.appuser;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Цей DTO використовується для генерації в базу мок-даними
 * та для уникнення використання додаткового
 * конструктора в entity AppUser.
 * Record не підходить, бо у ньому не стандартні
 * Getters та Setters які не підтримує modelMapper.
 */
@Data
@RequiredArgsConstructor
public class GenAppUser {

    private final String name;

    private final String surname;

    private final String username;

    private final String email;

    private final String address;

    private final String avatar;

    private final String headerPhoto;

    private final Integer dateOfBirth;

}
