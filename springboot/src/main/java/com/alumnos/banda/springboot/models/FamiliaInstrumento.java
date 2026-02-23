package com.alumnos.banda.springboot.models;

import javax.persistence.*;

@Entity
@Table(name = "familias")
public class FamiliaInstrumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String nombre;

    public FamiliaInstrumento() {}

    public FamiliaInstrumento(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}