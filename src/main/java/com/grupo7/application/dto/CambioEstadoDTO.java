package com.grupo7.application.dto;

import java.time.LocalDateTime;

// No entity imports here! Only DTOs.
// import com.grupo7.application.entity.CambioEstado; // REMOVE THIS
// import com.grupo7.application.entity.Estado;       // REMOVE THIS
// import com.grupo7.application.entity.Empleado;     // REMOVE THIS
// import com.grupo7.application.entity.EventoSismico; // REMOVE THIS

public class CambioEstadoDTO {

    private Long id;
    private EstadoDTO estado; // Change to EstadoDTO
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EmpleadoDTO responsable; // Change to EmpleadoDTO
    private EventoSismicoDTO eventoSismico; // Change to EventoSismicoDTO

    public CambioEstadoDTO() {
    }

    public CambioEstadoDTO(Long id, EstadoDTO estado, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                           EmpleadoDTO responsable, EventoSismicoDTO eventoSismico) {
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

    // --- Getters y setters (ensure they use DTO types) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoDTO getEstado() { // Return EstadoDTO
        return estado;
    }

    public void setEstado(EstadoDTO estado) { // Accept EstadoDTO
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

    public EmpleadoDTO getResponsable() { // Return EmpleadoDTO
        return responsable;
    }

    public void setResponsable(EmpleadoDTO responsable) { // Accept EmpleadoDTO
        this.responsable = responsable;
    }

    public EventoSismicoDTO getEventoSismico() { // Return EventoSismicoDTO
        return eventoSismico;
    }

    public void setEventoSismico(EventoSismicoDTO eventoSismico) { // Accept EventoSismicoDTO
        this.eventoSismico = eventoSismico;
    }
}