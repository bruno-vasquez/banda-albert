package com.alumnos.banda.springboot.models;

import javax.persistence.*;

@Entity
@Table(name="users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    // Encargado: solo su familia. Admin: null
    @ManyToOne
    @JoinColumn(name="familia_id")
    private FamiliaInstrumento familiaAsignada;

    public AppUser() {}

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public FamiliaInstrumento getFamiliaAsignada() { return familiaAsignada; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(Role role) { this.role = role; }
    public void setFamiliaAsignada(FamiliaInstrumento familiaAsignada) { this.familiaAsignada = familiaAsignada; }
}