package com.facebook.service;

import com.facebook.model.AppUser;
import com.facebook.repository.AppUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppUserService {

    private static final String USER_NOT_FOUND_ERROR_MSG = "User not found with id: ";


    private final AppUserRepository repo;

    public String[] findRolesById(Long id) {
        return repo.findById(id)
                .map(AppUser::getRoles)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MSG + id));
    }

    public String findUsernameById(Long id) {
        return repo.findById(id)
                .map(AppUser::getUsername)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MSG + id));
    }

    public String findPasswordById(Long id) {
        return repo.findById(id)
                .map(AppUser::getPassword)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MSG + id));
    }

    public Optional<AppUser> findByUsername(String username) {
        Optional<AppUser> appUser = repo.findByUsername(username);
        log.info(appUser);
        return appUser;

    }

    public Optional<AppUser> findById(Long id) {
        return repo.findById(id);

    }

    public Optional<AppUser> save(AppUser appUser)
            throws DataIntegrityViolationException {
        return Optional.of(repo.save(appUser));

    }
}
