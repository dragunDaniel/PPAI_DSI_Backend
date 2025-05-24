package com.grupo7.application.dto;

import java.time.LocalDateTime;

public class EventoSismicoDTO {

    private LocalDateTime fechaHoraOcurrencia;
    private Double latitudEpicentro;
    private Double longitudEpicentro;
    private Double latitudHipocentro;
    private Double longitudHipocentro;

    public EventoSismicoDTO() {
    }

    public EventoSismicoDTO(LocalDateTime fechaHoraOcurrencia, Double latitudEpicentro, Double longitudEpicentro,
                                  Double latitudHipocentro, Double longitudHipocentro) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
    }

    // Getters y setters
    public LocalDateTime getFechaHoraOcurrencia() {
        return fechaHoraOcurrencia;
    }

    public void setFechaHoraOcurrencia(LocalDateTime fechaHoraOcurrencia) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
    }

    public Double getLatitudEpicentro() {
        return latitudEpicentro;
    }

    public void setLatitudEpicentro(Double latitudEpicentro) {
        this.latitudEpicentro = latitudEpicentro;
    }

    public Double getLongitudEpicentro() {
        return longitudEpicentro;
    }

    public void setLongitudEpicentro(Double longitudEpicentro) {
        this.longitudEpicentro = longitudEpicentro;
    }

    public Double getLatitudHipocentro() {
        return latitudHipocentro;
    }

    public void setLatitudHipocentro(Double latitudHipocentro) {
        this.latitudHipocentro = latitudHipocentro;
    }

    public Double getLongitudHipocentro() {
        return longitudHipocentro;
    }

    public void setLongitudHipocentro(Double longitudHipocentro) {
        this.longitudHipocentro = longitudHipocentro;
    }
}
