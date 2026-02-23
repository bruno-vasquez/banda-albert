package com.alumnos.banda.springboot.models;

import javax.persistence.*;

@Entity
@Table(name = "instrumentos")
public class Instrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String codigo; // TP01, TB01...

    @ManyToOne(optional = false)
    @JoinColumn(name = "familia_id")
    private FamiliaInstrumento familia;

    private boolean propio = false;

    public Instrumento() {}

    public Integer getId() { return id; }
    public String getCodigo() { return codigo; }
    public FamiliaInstrumento getFamilia() { return familia; }
    public boolean isPropio() { return propio; }

    public void setId(Integer id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setFamilia(FamiliaInstrumento familia) { this.familia = familia; }
    public void setPropio(boolean propio) { this.propio = propio; }
}