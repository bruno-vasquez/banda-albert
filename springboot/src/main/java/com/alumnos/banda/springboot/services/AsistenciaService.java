package com.alumnos.banda.springboot.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.alumnos.banda.springboot.dto.AsistenciaDtos;
import com.alumnos.banda.springboot.models.Alumno;
import com.alumnos.banda.springboot.repositories.AlumnoRepository;
import com.alumnos.banda.springboot.repositories.AsistenciaRepository;
import com.alumnos.banda.springboot.security.CustomUserDetails;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistencias;
    private final AlumnoRepository alumnos;

    public AsistenciaService(AsistenciaRepository asistencias, AlumnoRepository alumnos) {
        this.asistencias = asistencias;
        this.alumnos = alumnos;
    }

    private boolean isAdmin(Authentication auth) {
        CustomUserDetails u = (CustomUserDetails) auth.getPrincipal();
        return "ROLE_ADMIN".equals(u.getRole());
    }

    private Integer familiaIdEncargado(Authentication auth) {
        CustomUserDetails u = (CustomUserDetails) auth.getPrincipal();
        return u.getFamiliaId();
    }

    public List<com.alumnos.banda.springboot.models.Asistencia> guardarBulk(LocalDate fecha, List<AsistenciaDtos.Item> items, Authentication auth) {
        List<com.alumnos.banda.springboot.models.Asistencia> out = new ArrayList<>();

        for (AsistenciaDtos.Item it : items) {
            Alumno a = alumnos.findById(it.alumnoId).orElseThrow(() -> new RuntimeException("Alumno no existe"));

            // Permiso: encargado solo su familia
            if (!isAdmin(auth)) {
                Integer famEnc = familiaIdEncargado(auth);
                Integer famAlu = a.getInstrumento() != null && a.getInstrumento().getFamilia() != null
                        ? a.getInstrumento().getFamilia().getId()
                        : null;
                if (famEnc == null || famAlu == null || !famEnc.equals(famAlu)) {
                    throw new RuntimeException("No autorizado para este alumno");
                }
            }

            // Evitar duplicados por fecha+alumno
            if (asistencias.existsByFechaAndAlumno_Id(fecha, a.getId())) {
                continue; // o lanza error, como prefieras
            }

            com.alumnos.banda.springboot.models.Asistencia as = new com.alumnos.banda.springboot.models.Asistencia();
            as.setFecha(fecha);
            as.setAlumno(a);
            as.setAsistio(it.asistio);

            // snapshots
            as.setFamiliaNombre(a.getFamiliaNombre() != null ? a.getFamiliaNombre() : "SIN_FAMILIA");
            as.setBanda(a.getBandaTexto());
            as.setNombreCompleto((a.getApellido() + " " + a.getNombre()).trim());
            as.setCursoTexto(a.getCursoTexto());

            out.add(asistencias.save(as));
        }

        return out;
    }

    public List<com.alumnos.banda.springboot.models.Asistencia> historialPorAlumno(Integer alumnoId, Authentication auth) {
        // Si quieres, aquí también pones validación de familia para encargado
        return asistencias.findByAlumno_IdOrderByFechaAsc(alumnoId);
    }
}