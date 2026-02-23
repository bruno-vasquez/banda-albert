package com.alumnos.banda.springboot.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alumnos.banda.springboot.models.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByFechaAndFamiliaNombre(LocalDate fecha, String familiaNombre);
    List<Asistencia> findByAlumno_IdOrderByFechaAsc(Integer alumnoId);
    boolean existsByFechaAndAlumno_Id(LocalDate fecha, Integer alumnoId);
}