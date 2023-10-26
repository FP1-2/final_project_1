package com.facebook.service;

import com.facebook.dto.appuser.AppUserChatResponse;
import com.facebook.dto.appuser.AppUserEditRequest;
import com.facebook.exception.UserNotFoundException;
import com.facebook.facade.AppUserFacade;
import com.facebook.model.AppUser;
import com.facebook.repository.AppUserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AppUserService {

    private static final String USER_NOT_FOUND_ERROR_MSG = "User not found with id: ";

    private final AppUserRepository repo;

    private final PasswordEncoder passwordEncoder;

    private final AppUserFacade facade;


    //Тільки для генерації.
    public List<AppUser> findAll() {
        return repo.findAll();
    }

    public Page<AppUser> findAllAppUsers(Pageable pageable) {
        return repo.findAll(pageable);
    }

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

    public Optional<AppUser> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public void updatePassword(String email, String pass) {
        findByEmail(email).ifPresentOrElse(u -> {
                    u.setPassword(passwordEncoder.encode(pass));
                    save(u);
                },
                () -> {
                    throw new UserNotFoundException();
                }
        );
    }

    public AppUser getAuthUser(){
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return getAuthUser(principal);
    }
    public AppUser getAuthUser(Principal principal){
        return findByUsername(principal.getName())
                .orElseThrow(UserNotFoundException::new);
    }
    public Optional<AppUser> editUser(Long id, AppUserEditRequest userEditReq) {
        return findById(id).flatMap(u -> {
            facade.updateAppUserFromEditRequest(u, userEditReq);
            return save(u);
        });
    }
    public AppUser convertToAppUser(String username){
        return findByUsername(username).map(i-> i).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
    public List<AppUserChatResponse> findUserByKeyword(String keyword, Pageable pageable){
        AppUser authUser = getAuthUser();
       return repo.searchUserByNameAndUsername(keyword, authUser.getId(), pageable)
               .stream()
               .map(u -> facade.convertToAppUserChatResponse(u))
               .toList();
    }
}
