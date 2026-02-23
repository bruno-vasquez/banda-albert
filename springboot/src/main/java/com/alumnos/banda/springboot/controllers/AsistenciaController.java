package com.alumnos.banda.springboot.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.alumnos.banda.springboot.dto.AsistenciaDtos;
import com.alumnos.banda.springboot.services.AsistenciaService;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "*")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

    // Bulk: toma asistencia de su familia (encargado) o cualquiera (admin)
    @PostMapping("/bulk")
    public List<com.alumnos.banda.springboot.models.Asistencia> bulk(@RequestBody AsistenciaDtos.BulkRequest req, Authentication auth) {
        return service.guardarBulk(req.fecha, req.items, auth);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<com.alumnos.banda.springboot.models.Asistencia> historial(@PathVariable Integer alumnoId, Authentication auth) {
        return service.historialPorAlumno(alumnoId, auth);
    }
}