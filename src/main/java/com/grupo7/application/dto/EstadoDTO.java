package com.grupo7.application.dto;

public class EstadoDTO {

    private Long id;
    private String ambito;
    private String nombreEstado;

    public EstadoDTO() {
    }

    public EstadoDTO(Long id, String ambito, String nombreEstado) {
        this.id = id;
        this.ambito = ambito;
        this.nombreEstado = nombreEstado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
}
