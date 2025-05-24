package com.grupo7.application.dto;

import java.time.LocalDateTime;

public class SerieTemporalDTO {

    private Long id;
    private String condicionAlarma;
    private LocalDateTime fechaHoraInicioRegMuestreo;
    private LocalDateTime fechaHoraRegistros;
    private Double frecuenciaMuestreo;
    private Long muestraSismicaId;
    private String sismografoId;
    private Long eventoSismicoId;

    public SerieTemporalDTO() {
    }

    public SerieTemporalDTO(Long id, String condicionAlarma, LocalDateTime fechaHoraInicioRegMuestreo,
                            LocalDateTime fechaHoraRegistros, Double frecuenciaMuestreo,
                            Long muestraSismicaId, String sismografoId, Long eventoSismicoId) {
        this.id = id;
        this.condicionAlarma = condicionAlarma;
        this.fechaHoraInicioRegMuestreo = fechaHoraInicioRegMuestreo;
        this.fechaHoraRegistros = fechaHoraRegistros;
        this.frecuenciaMuestreo = frecuenciaMuestreo;
        this.muestraSismicaId = muestraSismicaId;
        this.sismografoId = sismografoId;
        this.eventoSismicoId = eventoSismicoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCondicionAlarma() {
        return condicionAlarma;
    }

    public void setCondicionAlarma(String condicionAlarma) {
        this.condicionAlarma = condicionAlarma;
    }

    public LocalDateTime getFechaHoraInicioRegMuestreo() {
        return fechaHoraInicioRegMuestreo;
    }

    public void setFechaHoraInicioRegMuestreo(LocalDateTime fechaHoraInicioRegMuestreo) {
        this.fechaHoraInicioRegMuestreo = fechaHoraInicioRegMuestreo;
    }

    public LocalDateTime getFechaHoraRegistros() {
        return fechaHoraRegistros;
    }

    public void setFechaHoraRegistros(LocalDateTime fechaHoraRegistros) {
        this.fechaHoraRegistros = fechaHoraRegistros;
    }

    public Double getFrecuenciaMuestreo() {
        return frecuenciaMuestreo;
    }

    public void setFrecuenciaMuestreo(Double frecuenciaMuestreo) {
        this.frecuenciaMuestreo = frecuenciaMuestreo;
    }

    public Long getMuestraSismicaId() {
        return muestraSismicaId;
    }

    public void setMuestraSismicaId(Long muestraSismicaId) {
        this.muestraSismicaId = muestraSismicaId;
    }

    public String getSismografoId() {
        return sismografoId;
    }

    public void setSismografoId(String sismografoId) {
        this.sismografoId = sismografoId;
    }

    public Long getEventoSismicoId() {
        return eventoSismicoId;
    }

    public void setEventoSismicoId(Long eventoSismicoId) {
        this.eventoSismicoId = eventoSismicoId;
    }
}
