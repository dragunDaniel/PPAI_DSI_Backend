package com.grupo7.application.dto;

public class TipoDeDatoDTO {

    private Long id;
    private String denominacion;
    private String nombreUnidadMedida;
    private Double valorUmbral;

    // Constructor con todos los campos
    public TipoDeDatoDTO(Long id, String denominacion, String nombreUnidadMedida, Double valorUmbral) {
        this.id = id;
        this.denominacion = denominacion;
        this.nombreUnidadMedida = nombreUnidadMedida;
        this.valorUmbral = valorUmbral;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getNombreUnidadMedida() {
        return nombreUnidadMedida;
    }

    public void setNombreUnidadMedida(String nombreUnidadMedida) {
        this.nombreUnidadMedida = nombreUnidadMedida;
    }

    public Double getValorUmbral() {
        return valorUmbral;
    }

    public void setValorUmbral(Double valorUmbral) {
        this.valorUmbral = valorUmbral;
    }
}