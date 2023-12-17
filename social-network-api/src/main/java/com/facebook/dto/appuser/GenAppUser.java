package com.facebook.dto.appuser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Цей DTO використовується для генерації в базу мок-даними
 * та для уникнення використання додаткового
 * конструктора в entity AppUser.
 * Record не підходить, бо у ньому не стандартні
 * Getters та Setters які не підтримує modelMapper.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenAppUser {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String address;
    private String avatar;
    private String headerPhoto;
    private Integer dateOfBirth;
}
