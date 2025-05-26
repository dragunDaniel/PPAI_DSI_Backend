package com.grupo7.application.dto;

// Dependencies
import java.time.LocalDateTime;

// Entidades
import com.grupo7.application.entity.CambioEstado;
import com.grupo7.application.entity.Estado;
import com.grupo7.application.entity.Empleado;
import com.grupo7.application.entity.EventoSismico;

public class CambioEstadoDTO {

    private Long id;
    private Estado estado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Empleado responsable;
    private EventoSismico eventoSismico;

    public CambioEstadoDTO() {
    }

    public CambioEstadoDTO(Long id, Estado estado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                           Empleado responsable, EventoSismico eventoSismico) {
        this.id = id;
        this.estado = estado;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.responsable = responsable;
        this.eventoSismico = eventoSismico;
    }

    public boolean esEstadoActual() {
        return this.fechaHoraFin == null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
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

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public EventoSismico getEventoSismico() {
        return eventoSismico;
    }

    public void setEventoSismico(EventoSismico eventoSismico) {
        this.eventoSismico = eventoSismico;
    }
}
