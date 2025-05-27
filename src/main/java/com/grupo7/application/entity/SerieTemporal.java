package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "serie_temporal")
public class SerieTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERIE")
    private Long id;

    @Column(name = "CONDICION_ALARMA")
    private String condicionAlarma;

    @Column(name = "FECHA_HORA_INICIO_REG_MUESTREO")
    private LocalDateTime fechaHoraInicioRegMuestreo;

    @Column(name = "FECHA_HORA_REGISTROS")
    private LocalDateTime fechaHoraRegistros;

    @Column(name = "FRECUENCIA_MUESTREO")
    private Double frecuenciaMuestreo;

    @ManyToOne
    @JoinColumn(name = "ID_MUESTRA_SISMICA")
    private MuestraSismica muestraSismica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SISMOGRAFO", nullable = false)
    private Sismografo sismografo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENTO_SISMICO_ID", nullable = false)
    private EventoSismico eventoSismico;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCondicionAlarma() { return condicionAlarma; }
    public void setCondicionAlarma(String condicionAlarma) { this.condicionAlarma = condicionAlarma; }

    public LocalDateTime getFechaHoraInicioRegMuestreo() { return fechaHoraInicioRegMuestreo; }
    public void setFechaHoraInicioRegMuestreo(LocalDateTime fechaHoraInicioRegMuestreo) { this.fechaHoraInicioRegMuestreo = fechaHoraInicioRegMuestreo; }

    public LocalDateTime getFechaHoraRegistros() { return fechaHoraRegistros; }
    public void setFechaHoraRegistros(LocalDateTime fechaHoraRegistros) { this.fechaHoraRegistros = fechaHoraRegistros; }

    public Double getFrecuenciaMuestreo() { return frecuenciaMuestreo; }
    public void setFrecuenciaMuestreo(Double frecuenciaMuestreo) { this.frecuenciaMuestreo = frecuenciaMuestreo; }

    public MuestraSismica getMuestraSismica() { return muestraSismica; }
    public void setMuestraSismica(MuestraSismica muestraSismica) { this.muestraSismica = muestraSismica; }

    public Sismografo getSismografo() { return sismografo; }
    public void setSismografo(Sismografo sismografo) { this.sismografo = sismografo; }

    public EventoSismico getEventoSismico() { return eventoSismico; }
    public void setEventoSismico(EventoSismico eventoSismico) { this.eventoSismico = eventoSismico; }
}
