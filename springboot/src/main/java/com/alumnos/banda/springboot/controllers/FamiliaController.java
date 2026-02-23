package com.alumnos.banda.springboot.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.models.FamiliaInstrumento;
import com.alumnos.banda.springboot.services.FamiliaService;

@RestController
@RequestMapping("/api/familias")
@CrossOrigin(origins = "*")
public class FamiliaController {

    private final FamiliaService service;

    public FamiliaController(FamiliaService service) {
        this.service = service;
    }

    @GetMapping
    public List<FamiliaInstrumento> listar() {
        return service.listar();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public FamiliaInstrumento crear(@RequestBody FamiliaInstrumento f) {
        return service.crear(f);
    }
}