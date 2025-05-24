package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "magnitud_ritcher")
public class MagnitudRitcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MAGNITUD_RITCHER")
    private Long id;

    @Column(name = "NUMERO")
    private Double numero;

    @Column(name = "DESCRIPCION_MAGNITUD")
    private String descripcionMagnitud;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getNumero() { return numero; }
    public void setNumero(Double numero) { this.numero = numero; }

    public String getDescripcionMagnitud() { return descripcionMagnitud; }
    public void setDescripcionMagnitud(String descripcionMagnitud) { this.descripcionMagnitud = descripcionMagnitud; }
}
