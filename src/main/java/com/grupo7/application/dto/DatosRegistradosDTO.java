package com.grupo7.application.dto;

import java.util.List;
import java.util.ArrayList;

public class DatosRegistradosDTO {
    private String alcanceSismoNombre;
    private String clasificacionSismoNombre;
    private String origenGeneracionNombre;
    private List<SerieTemporalDetalleDTO> seriesTemporalesConDetalles;

    /**
     * Constructor for DatosRegistradosDTO.
     *
     * @param alcanceSismoNombre The name of the earthquake's reach.
     * @param clasificacionSismoNombre The name of the earthquake's classification.
     * @param origenGeneracionNombre The name of the earthquake's origin of generation.
     * @param seriesTemporalesConDetalles A list of SerieTemporalDetalleDTO,
     * representing the detailed time series data,
     * each containing its associated seismic samples and their details.
     */
    public DatosRegistradosDTO(String alcanceSismoNombre, String clasificacionSismoNombre,
                               String origenGeneracionNombre, List<SerieTemporalDetalleDTO> seriesTemporalesConDetalles) {
        this.alcanceSismoNombre = alcanceSismoNombre;
        this.clasificacionSismoNombre = clasificacionSismoNombre;
        this.origenGeneracionNombre = origenGeneracionNombre;
        // Ensure the list is not null, or initialize it if null is passed
        this.seriesTemporalesConDetalles = seriesTemporalesConDetalles != null ? seriesTemporalesConDetalles : new ArrayList<>();
    }

    // Getters

    public String getAlcanceSismoNombre() {
        return alcanceSismoNombre;
    }

    public String getClasificacionSismoNombre() {
        return clasificacionSismoNombre;
    }

    public String getOrigenGeneracionNombre() {
        return origenGeneracionNombre;
    }

    public List<SerieTemporalDetalleDTO> getSeriesTemporalesConDetalles() {
        return seriesTemporalesConDetalles;
    }

    // Setters (optional, but often useful for mappers or if building DTOs incrementally)

    public void setAlcanceSismoNombre(String alcanceSismoNombre) {
        this.alcanceSismoNombre = alcanceSismoNombre;
    }

    public void setClasificacionSismoNombre(String clasificacionSismoNombre) {
        this.clasificacionSismoNombre = clasificacionSismoNombre;
    }

    public void setOrigenGeneracionNombre(String origenGeneracionNombre) {
        this.origenGeneracionNombre = origenGeneracionNombre;
    }

    public void setSeriesTemporalesConDetalles(List<SerieTemporalDetalleDTO> seriesTemporalesConDetalles) {
        this.seriesTemporalesConDetalles = seriesTemporalesConDetalles != null ? seriesTemporalesConDetalles : new ArrayList<>();
    }

    /**
     * Provides a string representation of the DatosRegistradosDTO object,
     * useful for logging and debugging.
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "DatosRegistradosDTO{" +
                "alcanceSismoNombre='" + alcanceSismoNombre + '\'' +
                ", clasificacionSismoNombre='" + clasificacionSismoNombre + '\'' +
                ", origenGeneracionNombre='" + origenGeneracionNombre + '\'' +
                ", seriesTemporalesConDetalles=" + seriesTemporalesConDetalles +
                '}';
    }
}