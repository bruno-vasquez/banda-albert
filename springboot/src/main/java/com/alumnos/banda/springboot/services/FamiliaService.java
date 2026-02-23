package com.alumnos.banda.springboot.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.models.FamiliaInstrumento;
import com.alumnos.banda.springboot.repositories.FamiliaRepository;

@Service
public class FamiliaService {

    private final FamiliaRepository repo;

    public FamiliaService(FamiliaRepository repo) {
        this.repo = repo;
    }

    public List<FamiliaInstrumento> listar() {
        return repo.findAll();
    }

    public FamiliaInstrumento crear(FamiliaInstrumento f) {
        f.setId(null);
        return repo.save(f);
    }

    public FamiliaInstrumento obtener(Integer id) {
        return repo.findById(id).orElse(null);
    }
}