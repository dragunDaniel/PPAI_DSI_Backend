package com.grupo7.application.dto;

public class MagnitudRitcherDTO {

    private Long id;
    private Double numero;
    private String descripcionMagnitud;

    public MagnitudRitcherDTO() {
    }

    public MagnitudRitcherDTO(Long id, Double numero, String descripcionMagnitud) {
        this.id = id;
        this.numero = numero;
        this.descripcionMagnitud = descripcionMagnitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNumero() {
        return numero;
    }

    public void setNumero(Double numero) {
        this.numero = numero;
    }

    public String getDescripcionMagnitud() {
        return descripcionMagnitud;
    }

    public void setDescripcionMagnitud(String descripcionMagnitud) {
        this.descripcionMagnitud = descripcionMagnitud;
    }
}
