package com.alumnos.banda.springboot.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.models.AppUser;
import com.alumnos.banda.springboot.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository users;

    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Integer familiaId = (u.getFamiliaAsignada() != null ? u.getFamiliaAsignada().getId() : null);

        return new CustomUserDetails(u.getUsername(), u.getPasswordHash(), u.getRole().name(), familiaId);
    }
}