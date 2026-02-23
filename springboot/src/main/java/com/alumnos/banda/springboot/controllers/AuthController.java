package com.alumnos.banda.springboot.controllers;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.dto.AuthDtos;
import com.alumnos.banda.springboot.models.AppUser;
import com.alumnos.banda.springboot.repositories.UserRepository;
import com.alumnos.banda.springboot.security.CustomUserDetails;
import com.alumnos.banda.springboot.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserRepository users;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserRepository users) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.users = users;
    }

    @PostMapping("/login")
    public AuthDtos.LoginResponse login(@RequestBody AuthDtos.LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username, req.password)
        );

        CustomUserDetails ud = (CustomUserDetails) auth.getPrincipal();

        AppUser u = users.findByUsername(ud.getUsername()).orElseThrow();

        Integer familiaId = (u.getFamiliaAsignada() != null ? u.getFamiliaAsignada().getId() : null);
        String familiaNombre = (u.getFamiliaAsignada() != null ? u.getFamiliaAsignada().getNombre() : null);

        String token = jwtUtil.generateToken(ud.getUsername(), ud.getRole(), familiaId);
        return new AuthDtos.LoginResponse(token, ud.getUsername(), ud.getRole(), familiaId, familiaNombre);
    }
}