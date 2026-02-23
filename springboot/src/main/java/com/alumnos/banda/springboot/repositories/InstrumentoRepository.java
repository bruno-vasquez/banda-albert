package com.alumnos.banda.springboot.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alumnos.banda.springboot.models.Instrumento;

public interface InstrumentoRepository extends JpaRepository<Instrumento, Integer> {
    Optional<Instrumento> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}