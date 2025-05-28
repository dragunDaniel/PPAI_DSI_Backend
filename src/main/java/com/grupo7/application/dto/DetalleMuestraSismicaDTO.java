package com.grupo7.application.dto;

public class DetalleMuestraSismicaDTO {
    private Long id;
    private Long tipoDatoId; 
    private String denominacion;
    private Double valor;

    public DetalleMuestraSismicaDTO() {}

    public DetalleMuestraSismicaDTO(Long id, Long tipoDatoId, String denominacion, Double valor) {
        this.id = id;
        this.tipoDatoId = tipoDatoId;
        this.denominacion = denominacion;
        this.valor = valor;
    }

    // Getters
    public Long getId() { return id; }
    public Long getTipoDatoId() { return tipoDatoId; }
    public String getDenominacion() { return denominacion; }
    public Double getValor() { return valor; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTipoDatoId(Long tipoDatoId) { this.tipoDatoId = tipoDatoId; }
    public void setDenominacion(String denominacion) { this.denominacion = denominacion; }
    public void setValor(Double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "DetalleMuestraSismicaDTO{" +
               "id=" + id +
               ", tipoDatoId=" + tipoDatoId +
               ", denominacion='" + denominacion + '\'' +
               ", valor=" + valor +
               '}';
    }
}
