package com.alumnos.banda.springboot.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;
    private final Integer familiaId;

    public CustomUserDetails(String username, String password, String role, Integer familiaId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.familiaId = familiaId;
    }

    public Integer getFamiliaId() {
        return familiaId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}