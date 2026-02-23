package com.alumnos.banda.springboot.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alumnos.banda.springboot.models.*;

public interface FamiliaRepository extends JpaRepository<FamiliaInstrumento, Integer> {
    Optional<FamiliaInstrumento> findByNombre(String nombre);
}