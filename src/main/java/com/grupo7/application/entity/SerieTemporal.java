package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set; // Changed from List to Set

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SISMOGRAFO")
    private Sismografo sismografo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENTO_SISMICO_ID")
    private EventoSismico eventoSismico;

    // One SerieTemporal has many MuestraSismica
    @OneToMany(mappedBy = "serieTemporal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Add @BatchSize for performance if you keep List/Set and don't always JOIN FETCH
    // @org.hibernate.annotations.BatchSize(size = 20)
    private Set<MuestraSismica> muestrasSismicas = new HashSet<>(); // Changed to Set

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

    public Sismografo getSismografo() { return sismografo; }
    public void setSismografo(Sismografo sismografo) { this.sismografo = sismografo; }

    public EventoSismico getEventoSismico() { return eventoSismico; }
    public void setEventoSismico(EventoSismico eventoSismico) { this.eventoSismico = eventoSismico; }

    public Set<MuestraSismica> getMuestrasSismicas() { // Changed getter return type
        return muestrasSismicas;
    }

    public void setMuestrasSismicas(Set<MuestraSismica> muestrasSismicas) { // Changed setter parameter type
        this.muestrasSismicas = muestrasSismicas;
    }
}
