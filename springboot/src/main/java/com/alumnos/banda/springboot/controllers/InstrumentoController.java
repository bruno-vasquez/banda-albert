package com.alumnos.banda.springboot.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.models.Instrumento;
import com.alumnos.banda.springboot.services.InstrumentoService;

@RestController
@RequestMapping("/api/instrumentos")
@CrossOrigin(origins = "*")
public class InstrumentoController {

    private final InstrumentoService service;

    public InstrumentoController(InstrumentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Instrumento> listar() {
        return service.listar();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Instrumento crear(@RequestParam String codigo, @RequestParam Integer familiaId, @RequestParam(defaultValue = "false") boolean propio) {
        return service.crear(codigo, familiaId, propio);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public Instrumento editar(@PathVariable Integer id, @RequestParam String codigo, @RequestParam Integer familiaId, @RequestParam(defaultValue = "false") boolean propio) {
        return service.editar(id, codigo, familiaId, propio);
    }
}