package com.grupo7.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList; // For initializing lists to prevent NullPointerException

public class SerieTemporalDetalleDTO {
    private Long id; // Include ID to identify the series
    private String condicionAlarma;
    private LocalDateTime fechaHoraRegistros; // Or other relevant fields
    private String sismografoIdentificador; // Example: show sismografo detail
    private String codigoEstacion; // <-- ADDED THIS FIELD
    private List<MuestraSismicaDTO> muestrasSismicas; // List of nested MuestraSismica DTOs

    // Constructor
    public SerieTemporalDetalleDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraRegistros,
                                   String sismografoIdentificador, String codigoEstacion, // <-- ADDED TO CONSTRUCTOR
                                   List<MuestraSismicaDTO> muestrasSismicas) {
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.sismografoIdentificador = sismografoIdentificador;
        this.codigoEstacion = codigoEstacion; // <-- INITIALIZED HERE
        // Initialize list to prevent NullPointerException if null is passed
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    // Getters
    public Long getId() { return id; }
    public String getCondicionAlarma() { return condicionAlarma; }
    public LocalDateTime getFechaHoraRegistros() { return fechaHoraRegistros; }
    public String getSismografoIdentificador() { return sismografoIdentificador; }
    public String getCodigoEstacion() { return codigoEstacion; } // <-- NEW GETTER
    public List<MuestraSismicaDTO> getMuestrasSismicas() { return muestrasSismicas; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCondicionAlarma(String condicionAlarma) { this.condicionAlarma = condicionAlarma; }
    public void setFechaHoraRegistros(LocalDateTime fechaHoraRegistros) { this.fechaHoraRegistros = fechaHoraRegistros; }
    public void setSismografoIdentificador(String sismografoIdentificador) { this.sismografoIdentificador = sismografoIdentificador; }
    public void setCodigoEstacion(String codigoEstacion) { this.codigoEstacion = codigoEstacion; } // <-- NEW SETTER
    public void setMuestrasSismicas(List<MuestraSismicaDTO> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SerieTemporalDetalleDTO{" +
                "id=" + id +
                ", condicionAlarma='" + condicionAlarma + '\'' +
                ", fechaHoraRegistros=" + fechaHoraRegistros +
                ", sismografoIdentificador='" + sismografoIdentificador + '\'' +
                ", codigoEstacion='" + codigoEstacion + '\'' + // <-- INCLUDED IN toString()
                ", muestrasSismicas=" + muestrasSismicas +
                '}';
    }
}