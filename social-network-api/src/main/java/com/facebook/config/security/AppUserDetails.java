package com.facebook.config.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class AppUserDetails implements UserDetails {

    private final Integer id;
    private final String[] roles;
    private final String username;
    private final String password;
    private final Date expirationDate;

    public AppUserDetails(int id, String[] roles, String username,
                          String password, Date expirationDate) {
        this.id = id;
        this.roles = roles;
        this.username = username;
        this.password = password;
        this.expirationDate = expirationDate;
    }

    public static AppUserDetails of(int id, String[] roles, String username,
                                    String password, Date expirationDate){
        return new AppUserDetails(id, roles, username, password, expirationDate);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
    //Перевіряє, термін дії облікового запису.
    @Override
    public boolean isAccountNonExpired() {
        return expirationDate.after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
