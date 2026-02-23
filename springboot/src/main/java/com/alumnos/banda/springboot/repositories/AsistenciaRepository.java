package com.alumnos.banda.springboot.repositories;

import com.alumnos.banda.springboot.dto.AsistenciaPorFechaDTO;
import com.alumnos.banda.springboot.models.Asistencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    @Query(
        "SELECT new com.alumnos.banda.springboot.dto.AsistenciaPorFechaDTO(" +
        " a.id, a.fecha, " +
        " al.id, al.nombre, al.apellido, " +
        " i.id, i.codigo, " +
        " f.id, f.nombre, " +
        " a.asistio ) " +   // ✅ aquí
        "FROM Asistencia a " +
        "JOIN a.alumno al " +
        "LEFT JOIN al.instrumento i " +
        "LEFT JOIN i.familia f " +
        "WHERE a.fecha = :fecha " +
        "AND (:q IS NULL OR :q = '' OR " +
        " LOWER(al.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
        " LOWER(al.apellido) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
        " LOWER(i.codigo) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
        " LOWER(f.nombre) LIKE LOWER(CONCAT('%', :q, '%')) ) " +
        "AND (:familiaId IS NULL OR f.id = :familiaId) " +
        "AND (:instrumentoId IS NULL OR i.id = :instrumentoId) " +
        "AND (:asistio IS NULL OR a.asistio = :asistio)"  // ✅ aquí
    )
    Page<AsistenciaPorFechaDTO> buscarPorFechaConFiltros(
            @Param("fecha") LocalDate fecha,
            @Param("q") String q,
            @Param("familiaId") Integer familiaId,
            @Param("instrumentoId") Integer instrumentoId,
            @Param("asistio") Boolean asistio,  // ✅ aquí
            Pageable pageable
    );

    // ✅ Evitar duplicados por fecha+alumno
    boolean existsByFechaAndAlumno_Id(LocalDate fecha, Integer alumnoId);

    // ✅ Historial por alumno
    List<Asistencia> findByAlumno_IdOrderByFechaAsc(Integer alumnoId);
}