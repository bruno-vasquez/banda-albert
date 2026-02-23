package com.alumnos.banda.springboot.dto;

import java.time.LocalDate;

public class AsistenciaPorFechaDTO {

    private Long asistenciaId;
    private LocalDate fecha;

    private Integer alumnoId;
    private String nombre;
    private String apellido;

    private Integer instrumentoId;
    private String instrumentoNombre; // aquí llegará i.codigo

    private Integer familiaId;
    private String familiaNombre;

    private Boolean asistio;

    public AsistenciaPorFechaDTO(Long asistenciaId, LocalDate fecha,
                                 Integer alumnoId, String nombre, String apellido,
                                 Integer instrumentoId, String instrumentoNombre,
                                 Integer familiaId, String familiaNombre,
                                 Boolean asistio) {
        this.asistenciaId = asistenciaId;
        this.fecha = fecha;
        this.alumnoId = alumnoId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.instrumentoId = instrumentoId;
        this.instrumentoNombre = instrumentoNombre;
        this.familiaId = familiaId;
        this.familiaNombre = familiaNombre;
        this.asistio = asistio;
    }

    public Long getAsistenciaId() { return asistenciaId; }
    public LocalDate getFecha() { return fecha; }

    public Integer getAlumnoId() { return alumnoId; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }

    public Integer getInstrumentoId() { return instrumentoId; }
    public String getInstrumentoNombre() { return instrumentoNombre; }

    public Integer getFamiliaId() { return familiaId; }
    public String getFamiliaNombre() { return familiaNombre; }

    public Boolean getAsistio() { return asistio; }
}