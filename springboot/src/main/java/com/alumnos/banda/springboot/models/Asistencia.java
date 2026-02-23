package com.alumnos.banda.springboot.models;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table(name="asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalDate fecha;

    @ManyToOne(optional=false)
    @JoinColumn(name="alumno_id")
    private Alumno alumno;

    @Column(nullable=false)
    private boolean asistio;

    // snapshots
    @Column(nullable=false)
    private String familiaNombre;

    @Column(nullable=false)
    private String banda; // MUSICA / GUERRA / AMBAS / NINGUNA

    @Column(nullable=false)
    private String nombreCompleto;

    @Column(nullable=false)
    private String cursoTexto;

    public Asistencia() {}

    public Long getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public Alumno getAlumno() { return alumno; }
    public boolean isAsistio() { return asistio; }
    public String getFamiliaNombre() { return familiaNombre; }
    public String getBanda() { return banda; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getCursoTexto() { return cursoTexto; }

    public void setId(Long id) { this.id = id; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public void setAsistio(boolean asistio) { this.asistio = asistio; }
    public void setFamiliaNombre(String familiaNombre) { this.familiaNombre = familiaNombre; }
    public void setBanda(String banda) { this.banda = banda; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setCursoTexto(String cursoTexto) { this.cursoTexto = cursoTexto; }
}