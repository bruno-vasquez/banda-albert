package com.alumnos.banda.springboot.controllers;

import com.alumnos.banda.springboot.dto.AsistenciaDtos;
import com.alumnos.banda.springboot.dto.AsistenciaPorFechaDTO;
import com.alumnos.banda.springboot.repositories.AsistenciaRepository;
import com.alumnos.banda.springboot.services.AsistenciaService;
import com.alumnos.banda.springboot.models.Asistencia;

import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "*")
public class AsistenciaController {

    private final AsistenciaService service;
    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaController(AsistenciaService service, AsistenciaRepository asistenciaRepository) {
        this.service = service;
        this.asistenciaRepository = asistenciaRepository;
    }

    // Bulk: toma asistencia de su familia (encargado) o cualquiera (admin)
    @PostMapping("/bulk")
    public List<Asistencia> bulk(@RequestBody AsistenciaDtos.BulkRequest req, Authentication auth) {
        return service.guardarBulk(req.fecha, req.items, auth);
    }

    @GetMapping("/alumno/{alumnoId}")
    public List<Asistencia> historial(@PathVariable Integer alumnoId, Authentication auth) {
        return service.historialPorAlumno(alumnoId, auth);
    }

    // GET /api/asistencias/por-fecha?fecha=2026-02-23&q=bruno&familiaId=1&instrumentoId=2&presente=true&page=0&size=20
        @GetMapping("/por-fecha")
public Page<AsistenciaPorFechaDTO> porFecha(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Integer familiaId,
        @RequestParam(required = false) Integer instrumentoId,
        @RequestParam(required = false) Boolean asistio,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
) {
    Pageable pageable = PageRequest.of(page, size);

    return asistenciaRepository.buscarPorFechaConFiltros(
            fecha, q, familiaId, instrumentoId, asistio, pageable
    );
}
}