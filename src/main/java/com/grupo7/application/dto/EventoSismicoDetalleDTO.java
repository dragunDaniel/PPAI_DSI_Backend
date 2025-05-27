package com.grupo7.application.dto;

import java.util.List;

public class EventoSismicoDetalleDTO { // A more descriptive name than DatosRegistradosDTO
    private String alcanceSismoNombre;
    private String clasificacionSismoNombre;
    private String origenGeneracionNombre;
    private List<SerieTemporalDetalleDTO> seriesTemporales; // List of nested SerieTemporal DTOs

    public EventoSismicoDetalleDTO(String alcance, String clasificacion, String origen, List<SerieTemporalDetalleDTO> series) {
        this.alcanceSismoNombre = alcance;
        this.clasificacionSismoNombre = clasificacion;
        this.origenGeneracionNombre = origen;
        this.seriesTemporales = series;
    }

    // Getters
    public String getAlcanceSismoNombre() { return alcanceSismoNombre; }
    public String getClasificacionSismoNombre() { return clasificacionSismoNombre; }
    public String getOrigenGeneracionNombre() { return origenGeneracionNombre; }
    public List<SerieTemporalDetalleDTO> getSeriesTemporales() { return seriesTemporales; }

    // Setters (if needed by mappers)
    public void setAlcanceSismoNombre(String alcanceSismoNombre) { this.alcanceSismoNombre = alcanceSismoNombre; }
    public void setClasificacionSismoNombre(String clasificacionSismoNombre) { this.clasificacionSismoNombre = clasificacionSismoNombre; }
    public void setOrigenGeneracionNombre(String origenGeneracionNombre) { this.origenGeneracionNombre = origenGeneracionNombre; }
    public void setSeriesTemporales(List<SerieTemporalDetalleDTO> seriesTemporales) { this.seriesTemporales = seriesTemporales; }
}