package com.alumnos.banda.springboot.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.dto.UserCreateDto;
import com.alumnos.banda.springboot.models.*;
import com.alumnos.banda.springboot.repositories.*;

@Service
public class UserService {

    private final UserRepository users;
    private final FamiliaRepository familias;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, FamiliaRepository familias, PasswordEncoder encoder) {
        this.users = users;
        this.familias = familias;
        this.encoder = encoder;
    }

    public AppUser crear(UserCreateDto dto) {
        if (users.existsByUsername(dto.username)) throw new RuntimeException("Username ya existe");

        AppUser u = new AppUser();
        u.setUsername(dto.username);
        u.setPasswordHash(encoder.encode(dto.password));

        if ("ADMIN".equalsIgnoreCase(dto.role)) {
            u.setRole(Role.ROLE_ADMIN);
            u.setFamiliaAsignada(null);
        } else {
            u.setRole(Role.ROLE_ENCARGADO);
            if (dto.familiaId == null) throw new RuntimeException("familiaId requerido para ENCARGADO");
            FamiliaInstrumento fam = familias.findById(dto.familiaId).orElseThrow(() -> new RuntimeException("Familia no existe"));
            u.setFamiliaAsignada(fam);
        }

        return users.save(u);
    }
}