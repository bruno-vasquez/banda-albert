package com.alumnos.banda.springboot.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alumnos.banda.springboot.models.Alumno;
import com.alumnos.banda.springboot.models.EstadoAlumno;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
    List<Alumno> findByEstado(EstadoAlumno estado);
    List<Alumno> findByInstrumento_Familia_IdAndEstado(Integer familiaId, EstadoAlumno estado);
}