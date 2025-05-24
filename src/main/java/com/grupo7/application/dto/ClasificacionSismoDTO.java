package com.grupo7.application.dto;

public class ClasificacionSismoDTO {

    private Long id;
    private Double kmProfundidadDesde;
    private Double kmProfundidadHasta;
    private String nombre;

    public ClasificacionSismoDTO() {
    }

    public ClasificacionSismoDTO(Long id, Double kmProfundidadDesde, Double kmProfundidadHasta, String nombre) {
        this.id = id;
        this.kmProfundidadDesde = kmProfundidadDesde;
        this.kmProfundidadHasta = kmProfundidadHasta;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKmProfundidadDesde() {
        return kmProfundidadDesde;
    }

    public void setKmProfundidadDesde(Double kmProfundidadDesde) {
        this.kmProfundidadDesde = kmProfundidadDesde;
    }

    public Double getKmProfundidadHasta() {
        return kmProfundidadHasta;
    }

    public void setKmProfundidadHasta(Double kmProfundidadHasta) {
        this.kmProfundidadHasta = kmProfundidadHasta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
