package com.alumnos.banda.springboot.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.models.*;
import com.alumnos.banda.springboot.repositories.*;

@Service
public class InstrumentoService {

    private final InstrumentoRepository instrumentos;
    private final FamiliaRepository familias;

    public InstrumentoService(InstrumentoRepository instrumentos, FamiliaRepository familias) {
        this.instrumentos = instrumentos;
        this.familias = familias;
    }

    public List<Instrumento> listar() {
        return instrumentos.findAll();
    }

    public Instrumento crear(String codigo, Integer familiaId, boolean propio) {
        if (instrumentos.existsByCodigo(codigo)) {
            throw new RuntimeException("Código de instrumento duplicado");
        }
        FamiliaInstrumento fam = familias.findById(familiaId).orElseThrow(() -> new RuntimeException("Familia no existe"));

        Instrumento i = new Instrumento();
        i.setCodigo(codigo);
        i.setFamilia(fam);
        i.setPropio(propio);
        return instrumentos.save(i);
    }

    public Instrumento editar(Integer id, String codigo, Integer familiaId, boolean propio) {
        Instrumento i = instrumentos.findById(id).orElseThrow(() -> new RuntimeException("Instrumento no existe"));

        if (!i.getCodigo().equals(codigo) && instrumentos.existsByCodigo(codigo)) {
            throw new RuntimeException("Código de instrumento duplicado");
        }

        FamiliaInstrumento fam = familias.findById(familiaId).orElseThrow(() -> new RuntimeException("Familia no existe"));

        i.setCodigo(codigo);
        i.setFamilia(fam);
        i.setPropio(propio);
        return instrumentos.save(i);
    }
}