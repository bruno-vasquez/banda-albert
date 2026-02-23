package com.alumnos.banda.springboot.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.models.Alumno;
import com.alumnos.banda.springboot.services.AlumnoService;

@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "*")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    // Admin: ve todos activos | Encargado: solo su familia
    @GetMapping
    public List<Alumno> listarActivos(Authentication auth) {
        return service.listarActivos(auth);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtener(@PathVariable Integer id, Authentication auth) {
        Alumno a = service.obtenerPorId(id, auth);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(a);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public Alumno crear(@RequestBody Alumno alumno) {
        return service.crear(alumno);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> editar(@PathVariable Integer id, @RequestBody Alumno alumno) {
        Alumno actualizado = service.editar(id, alumno);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }

    // Retirar sin borrar
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/retirar")
    public ResponseEntity<Alumno> retirar(@PathVariable Integer id) {
        Alumno a = service.retirar(id);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(a);
    }
}