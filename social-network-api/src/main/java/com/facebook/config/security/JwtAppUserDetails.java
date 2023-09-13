package com.facebook.config.security;

import com.facebook.model.AppUser;
import com.facebook.service.AppUserService;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class JwtAppUserDetails implements UserDetails {

    private final AppUserService service;

    private final Integer id;

    private final String[] roles;

    public JwtAppUserDetails(int id, AppUserService service) {
        this.service = service;
        this.id = id;
        this.roles = fetchRoles(id, service);
    }

    private static String[] fetchRoles(int id, AppUserService service) {
        return service.findById((long) id)
                .map(AppUser::getRoles)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
