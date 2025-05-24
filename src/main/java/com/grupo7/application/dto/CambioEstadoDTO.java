package com.grupo7.application.dto;

import java.time.LocalDateTime;

public class CambioEstadoDTO {

    private Long id;
    private Long estadoId;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Long responsableId;
    private Long eventoSismicoId;

    public CambioEstadoDTO() {
    }

    public CambioEstadoDTO(Long id, Long estadoId, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                           Long responsableId, Long eventoSismicoId) {
        this.id = id;
        this.estadoId = estadoId;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsableId = responsableId;
        this.eventoSismicoId = eventoSismicoId;
    }

    public boolean esEstadoActual() {
        return fechaHoraFin == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public Long getResponsableId() {
        return responsableId;
    }

    public void setResponsableId(Long responsableId) {
        this.responsableId = responsableId;
    }

    public Long getEventoSismicoId() {
        return eventoSismicoId;
    }

    public void setEventoSismicoId(Long eventoSismicoId) {
        this.eventoSismicoId = eventoSismicoId;
    }
}
