package com.facebook.service;

import com.facebook.config.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервіс, призначений для отримання інформації
 * про поточного користувача, що пройшов автентифікацію.
 */
@Service
public class CurrentUserService {

    /**
     * Отримує ідентифікатор поточного автентифікованого користувача.
     *
     * @return ідентифікатор поточного користувача або {@code null},
     * якщо користувач не автентифікований
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getId().longValue();
    }

}
