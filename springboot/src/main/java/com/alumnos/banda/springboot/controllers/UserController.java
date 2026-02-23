package com.alumnos.banda.springboot.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.dto.UserCreateDto;
import com.alumnos.banda.springboot.models.AppUser;
import com.alumnos.banda.springboot.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public AppUser crear(@RequestBody UserCreateDto dto) {
        return service.crear(dto);
    }
}