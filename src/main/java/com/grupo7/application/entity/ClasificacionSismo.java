package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clasificacion_sismo")
public class ClasificacionSismo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLASIFICACION")
    private Long id;

    @Column(name = "KM_PROFUNDIDAD_DESDE")
    private Double kmProfundidadDesde;

    @Column(name = "KM_PROFUNDIDAD_HASTA")
    private Double kmProfundidadHasta;

    @Column(name = "NOMBRE")
    private String nombre;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getKmProfundidadDesde() { return kmProfundidadDesde; }
    public void setKmProfundidadDesde(Double kmProfundidadDesde) { this.kmProfundidadDesde = kmProfundidadDesde; }

    public Double getKmProfundidadHasta() { return kmProfundidadHasta; }
    public void setKmProfundidadHasta(Double kmProfundidadHasta) { this.kmProfundidadHasta = kmProfundidadHasta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
