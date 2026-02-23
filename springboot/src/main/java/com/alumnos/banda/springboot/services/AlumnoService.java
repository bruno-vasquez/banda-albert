package com.alumnos.banda.springboot.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.models.*;
import com.alumnos.banda.springboot.repositories.AlumnoRepository;
import com.alumnos.banda.springboot.repositories.InstrumentoRepository;
import com.alumnos.banda.springboot.security.CustomUserDetails;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnos;
    private final InstrumentoRepository instrumentos;

    public AlumnoService(AlumnoRepository alumnos, InstrumentoRepository instrumentos) {
        this.alumnos = alumnos;
        this.instrumentos = instrumentos;
    }

    private boolean isAdmin(Authentication auth) {
        CustomUserDetails u = (CustomUserDetails) auth.getPrincipal();
        return "ROLE_ADMIN".equals(u.getRole());
    }

    private Integer familiaIdEncargado(Authentication auth) {
        CustomUserDetails u = (CustomUserDetails) auth.getPrincipal();
        return u.getFamiliaId();
    }

    public List<Alumno> listarActivos(Authentication auth) {
        if (isAdmin(auth)) {
            return alumnos.findByEstado(EstadoAlumno.ACTIVO);
        }
        Integer famId = familiaIdEncargado(auth);
        return alumnos.findByInstrumento_Familia_IdAndEstado(famId, EstadoAlumno.ACTIVO);
    }

    public Alumno obtenerPorId(Integer id, Authentication auth) {
        Alumno a = alumnos.findById(id).orElse(null);
        if (a == null) return null;

        if (isAdmin(auth)) return a;

        Integer famId = familiaIdEncargado(auth);
        Integer alumnoFam = (a.getInstrumento() != null && a.getInstrumento().getFamilia() != null)
                ? a.getInstrumento().getFamilia().getId()
                : null;

        return (famId != null && famId.equals(alumnoFam)) ? a : null;
    }

    public Alumno crear(Alumno nuevo) {
        nuevo.setId(null);
        // cargar instrumento por ID si viene solo id
        if (nuevo.getInstrumento() != null && nuevo.getInstrumento().getId() != null) {
            Instrumento inst = instrumentos.findById(nuevo.getInstrumento().getId())
                    .orElseThrow(() -> new RuntimeException("Instrumento no existe"));
            nuevo.setInstrumento(inst);
        }
        return alumnos.save(nuevo);
    }

    public Alumno editar(Integer id, Alumno nuevo) {
        Alumno guardado = alumnos.findById(id).orElse(null);
        if (guardado == null) return null;

        guardado.setNombre(nuevo.getNombre());
        guardado.setApellido(nuevo.getApellido());
        guardado.setCelularAlumno(nuevo.getCelularAlumno());
        guardado.setCelularApoderado(nuevo.getCelularApoderado());
        guardado.setCurso(nuevo.getCurso());
        guardado.setParalelo(nuevo.getParalelo());
        guardado.setGrado(nuevo.getGrado());

        if (nuevo.getInstrumento() != null && nuevo.getInstrumento().getId() != null) {
            Instrumento inst = instrumentos.findById(nuevo.getInstrumento().getId())
                    .orElseThrow(() -> new RuntimeException("Instrumento no existe"));
            guardado.setInstrumento(inst);
        } else {
            guardado.setInstrumento(null);
        }

        guardado.setAntiguedad(nuevo.isAntiguedad());
        guardado.setBandaMusica(nuevo.isBandaMusica());
        guardado.setBandaGuerra(nuevo.isBandaGuerra());
        guardado.setExtras(nuevo.getExtras());

        return alumnos.save(guardado);
    }

    public Alumno retirar(Integer id) {
        Alumno a = alumnos.findById(id).orElse(null);
        if (a == null) return null;
        a.setEstado(EstadoAlumno.RETIRADO);
        return alumnos.save(a);
    }
}