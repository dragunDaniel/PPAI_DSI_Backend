package com.grupo7.application.dto;

import java.time.LocalDateTime;

public class EventoSismicoDTO {

    private Long id;
    private LocalDateTime fechaHoraOcurrencia;
    private Double latitudEpicentro;
    private Double longitudEpicentro;
    private Double latitudHipocentro;
    private Double longitudHipocentro;
    private Double valorMagnitud;

    public EventoSismicoDTO() {
    }

    public EventoSismicoDTO(LocalDateTime fechaHoraOcurrencia, Double latitudEpicentro, Double longitudEpicentro,
                             Double latitudHipocentro, Double longitudHipocentro, Double valorMagnitud) {
        this.fechaHoraOcurrencia = fechaHoraOcurrencia;
        this.latitudEpicentro = latitudEpicentro;
        this.longitudEpicentro = longitudEpicentro;
        this.latitudHipocentro = latitudHipocentro;
        this.longitudHipocentro = longitudHipocentro;
        this.valorMagnitud = valorMagnitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getValorMagnitud() {
        return valorMagnitud;
    }

    public void setValorMagnitud(Double valorMagnitud) {
        this.valorMagnitud = valorMagnitud;
    }

}