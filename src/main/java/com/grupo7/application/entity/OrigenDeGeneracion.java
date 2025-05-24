package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "origen_de_generacion")
public class OrigenDeGeneracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ORIGEN")
    private Long id;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "NOMBRE")
    private String nombre;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
