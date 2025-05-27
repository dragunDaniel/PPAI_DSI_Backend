package com.grupo7.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class SerieTemporalDTO {
    private Long id;
    private String condicionAlarma;
    private LocalDateTime fechaHoraRegistros;
    private String sismografoIdentificador;
    private String codigoEstacion; // <-- Add this field
    private List<MuestraSismicaDTO> muestrasSismicas;

    public SerieTemporalDTO() {
        this.muestrasSismicas = new ArrayList<>();
    }

    public SerieTemporalDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraRegistros,
                            String sismografoIdentificador, String codigoEstacion) { // <-- Update constructor
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.sismografoIdentificador = sismografoIdentificador;
        this.codigoEstacion = codigoEstacion; // <-- Initialize
        this.muestrasSismicas = new ArrayList<>();
    }

    public SerieTemporalDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraRegistros,
                            String sismografoIdentificador, String codigoEstacion, // <-- Update constructor
                            List<MuestraSismicaDTO> muestrasSismicas) {
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.sismografoIdentificador = sismografoIdentificador;
        this.codigoEstacion = codigoEstacion; // <-- Initialize
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    // Getters
    public Long getId() { return id; }
    public String getCondicionAlarma() { return condicionAlarma; }
    public LocalDateTime getFechaHoraRegistros() { return fechaHoraRegistros; }
    public String getSismografoIdentificador() { return sismografoIdentificador; }
    public String getCodigoEstacion() { return codigoEstacion; } // <-- New getter
    public List<MuestraSismicaDTO> getMuestrasSismicas() { return muestrasSismicas; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCondicionAlarma(String condicionAlarma) { this.condicionAlarma = condicionAlarma; }
    public void setFechaHoraRegistros(LocalDateTime fechaHoraRegistros) { this.fechaHoraRegistros = fechaHoraRegistros; }
    public void setSismografoIdentificador(String sismografoIdentificador) { this.sismografoIdentificador = sismografoIdentificador; }
    public void setCodigoEstacion(String codigoEstacion) { this.codigoEstacion = codigoEstacion; } // <-- New setter
    public void setMuestrasSismicas(List<MuestraSismicaDTO> muestrasSismicas) {
        this.muestrasSismicas = muestrasSismicas != null ? muestrasSismicas : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SerieTemporalDTO{" +
                "id=" + id +
                ", condicionAlarma='" + condicionAlarma + '\'' +
                ", fechaHoraRegistros=" + fechaHoraRegistros +
                ", sismografoIdentificador='" + sismografoIdentificador + '\'' +
                ", codigoEstacion='" + codigoEstacion + '\'' + // <-- Include in toString
                ", muestrasSismicas=" + muestrasSismicas +
                '}';
    }
}