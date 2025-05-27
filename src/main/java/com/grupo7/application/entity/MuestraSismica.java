package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set; // Changed from List to Set

@Entity
@Table(name = "muestra_sismica")
public class MuestraSismica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MUESTRA_SISMICA")
    private Long id;

    @Column(name = "FECHA_HORA_MUESTRA")
    private LocalDateTime fechaHoraMuestra;

    // Many MuestraSismica to one SerieTemporal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SERIE") // Matches the foreign key in schema.sql
    private SerieTemporal serieTemporal;

    // One MuestraSismica has many DetalleMuestraSismica
    @OneToMany(mappedBy = "muestraSismica", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Add @BatchSize for performance if you keep List/Set and don't always JOIN FETCH
    // @org.hibernate.annotations.BatchSize(size = 20)
    private Set<DetalleMuestraSismica> detallesMuestra = new HashSet<>(); // Changed to Set

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHoraMuestra() { return fechaHoraMuestra; }
    public void setFechaHoraMuestra(LocalDateTime fechaHoraMuestra) { this.fechaHoraMuestra = fechaHoraMuestra; }

    public SerieTemporal getSerieTemporal() {
        return serieTemporal;
    }

    public void setSerieTemporal(SerieTemporal serieTemporal) {
        this.serieTemporal = serieTemporal;
    }

    public Set<DetalleMuestraSismica> getDetallesMuestra() { // Changed getter return type
        return detallesMuestra;
    }

    public void setDetallesMuestra(Set<DetalleMuestraSismica> detallesMuestra) { // Changed setter parameter type
        this.detallesMuestra = detallesMuestra;
    }
}
