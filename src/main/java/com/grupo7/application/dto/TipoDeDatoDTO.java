package com.grupo7.application.dto;

// Dependencies
import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoDeDatoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // Solo lectura para evitar seteos
    private Long id;

    private String denominacion;
    private String nombreUnidadMedida;
    private Double valorUmbral;

    // Constructor con todos los campos
    public TipoDeDatoDTO(String denominacion, String nombreUnidadMedida, Double valorUmbral) {
        this.denominacion = denominacion;
        this.nombreUnidadMedida = nombreUnidadMedida;
        this.valorUmbral = valorUmbral;
    }

    public boolean esTuDenominacion(String denominacion) {
        return this.denominacion.equals(denominacion);
    }

    // Getters y Setters
    public Long getId() { // Sin Setter, PK Autoincremental
        return id;
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