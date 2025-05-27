package com.grupo7.application.dto;

import java.time.LocalDateTime;

public class MuestraSismicaDTO {

    private Long id;
    private LocalDateTime fechaHoraMuestra;
    private Long detalleMuestraSismicaId;

    public MuestraSismicaDTO() {}

    public MuestraSismicaDTO(Long id, LocalDateTime fechaHoraMuestra, Long detalleMuestraSismicaId) {
        this.id = id;
        this.fechaHoraMuestra = fechaHoraMuestra;
        this.detalleMuestraSismicaId = detalleMuestraSismicaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHoraMuestra() { return fechaHoraMuestra; }
    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) { this.fechaHoraMuestra = fechaHoraMuestra; }

    public Long getDetalleMuestraSismicaId() { return detalleMuestraSismicaId; }
    public void setDetalleMuestraSismicaId(Long detalleMuestraSismicaId) { this.detalleMuestraSismicaId = detalleMuestraSismicaId; }
}