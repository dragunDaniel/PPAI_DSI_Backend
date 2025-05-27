package com.grupo7.application.dto;

import java.time.LocalDateTime;
import java.util.List; // To hold the collection of DetalleMuestraSismicaDTOs
import java.util.ArrayList; // For initializing the list

public class MuestraSismicaDTO {

    private Long id;
    private LocalDateTime fechaHoraMuestra;
    private List<DetalleMuestraSismicaDTO> detallesMuestra; // Renamed to clearly reflect the entity's collection

    public MuestraSismicaDTO() {
        this.detallesMuestra = new ArrayList<>(); // Initialize to prevent NullPointerException
    }

    public MuestraSismicaDTO(Long id, LocalDateTime fechaHoraMuestra, List<DetalleMuestraSismicaDTO> detallesMuestra) {
        this.id = id;
        this.fechaHoraMuestra = fechaHoraMuestra;
        this.detallesMuestra = detallesMuestra != null ? detallesMuestra : new ArrayList<>();
    }

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getFechaHoraMuestra() { return fechaHoraMuestra; }
    public List<DetalleMuestraSismicaDTO> getDetallesMuestra() { return detallesMuestra; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) { this.fechaHoraMuestra = fechaHoraMuestra; }
    public void setDetallesMuestra(List<DetalleMuestraSismicaDTO> detallesMuestra) {
        this.detallesMuestra = detallesMuestra != null ? detallesMuestra : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "MuestraSismicaDTO{" +
               "id=" + id +
               ", fechaHoraMuestra=" + fechaHoraMuestra +
               ", detallesMuestra=" + detallesMuestra +
               '}';
    }
}
