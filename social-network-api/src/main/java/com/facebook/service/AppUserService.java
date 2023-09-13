package com.facebook.service;

import com.facebook.model.AppUser;
import com.facebook.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repo;

    public Optional<AppUser> findByName(String name) {
        return repo.findByName(name);

    }

    public Optional<AppUser> findById(Long id) {
        return repo.findById(id);

    }

    public Optional<AppUser> save(AppUser appUser)
            throws DataIntegrityViolationException {
        return Optional.of(repo.save(appUser));

    }
}
