package com.grupo7.application.dto;

public class DetalleMuestraSismicaDTO {

    private Long id;
    private Long tipoDeDatoId;
    private Double valor;

    public DetalleMuestraSismicaDTO() {
    }

    public DetalleMuestraSismicaDTO(Long id, Long tipoDeDatoId, Double valor) {
        this.id = id;
        this.tipoDeDatoId = tipoDeDatoId;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipoDeDatoId() {
        return tipoDeDatoId;
    }

    public void setTipoDeDatoId(Long tipoDeDatoId) {
        this.tipoDeDatoId = tipoDeDatoId;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
