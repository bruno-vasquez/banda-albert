package com.alumnos.banda.springboot.models;

import javax.persistence.*;

@Entity
@Table(name="alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido;

    private String celularAlumno;
    private String celularApoderado;

    private Integer curso;
    private String paralelo;
    private String grado;

    @ManyToOne
    @JoinColumn(name="instrumento_id")
    private Instrumento instrumento;

    @Enumerated(EnumType.STRING)
    private EstadoAlumno estado = EstadoAlumno.ACTIVO;

    private boolean antiguedad;
    private boolean bandaMusica;
    private boolean bandaGuerra;

    private String extras;

    public Alumno() {}

    public Integer getId() { return id; }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCelularAlumno() { return celularAlumno; }
    public String getCelularApoderado() { return celularApoderado; }
    public Integer getCurso() { return curso; }
    public String getParalelo() { return paralelo; }
    public String getGrado() { return grado; }
    public Instrumento getInstrumento() { return instrumento; }
    public EstadoAlumno getEstado() { return estado; }
    public boolean isAntiguedad() { return antiguedad; }
    public boolean isBandaMusica() { return bandaMusica; }
    public boolean isBandaGuerra() { return bandaGuerra; }
    public String getExtras() { return extras; }

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setCelularAlumno(String celularAlumno) { this.celularAlumno = celularAlumno; }
    public void setCelularApoderado(String celularApoderado) { this.celularApoderado = celularApoderado; }
    public void setCurso(Integer curso) { this.curso = curso; }
    public void setParalelo(String paralelo) { this.paralelo = paralelo; }
    public void setGrado(String grado) { this.grado = grado; }
    public void setInstrumento(Instrumento instrumento) { this.instrumento = instrumento; }
    public void setEstado(EstadoAlumno estado) { this.estado = estado; }
    public void setAntiguedad(boolean antiguedad) { this.antiguedad = antiguedad; }
    public void setBandaMusica(boolean bandaMusica) { this.bandaMusica = bandaMusica; }
    public void setBandaGuerra(boolean bandaGuerra) { this.bandaGuerra = bandaGuerra; }
    public void setExtras(String extras) { this.extras = extras; }

    // Calculados (NO persistentes)
    @Transient
    public String getBandaTexto() {
        if (bandaMusica && bandaGuerra) return "AMBAS";
        if (bandaMusica) return "MUSICA";
        if (bandaGuerra) return "GUERRA";
        return "NINGUNA";
    }

    @Transient
    public String getFamiliaNombre() {
        return instrumento != null && instrumento.getFamilia() != null ? instrumento.getFamilia().getNombre() : null;
    }

    @Transient
    public String getCursoTexto() {
        // Ajusta a tu formato
        String c = (curso != null ? String.valueOf(curso) : "");
        String p = (paralelo != null ? paralelo : "");
        String g = (grado != null ? grado : "");
        return (c + " " + p + " " + g).trim();
    }
}