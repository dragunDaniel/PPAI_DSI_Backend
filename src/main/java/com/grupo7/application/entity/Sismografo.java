package com.grupo7.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sismografo")
public class Sismografo {

    @Id
    @Column(name = "IDENTIFICADOR")
    private String identificador;

    @Column(name = "ID_ADQUISICION")
    private Integer idAdquisicion;

    @Column(name = "NRO_SERIE")
    private String nroSerie;

    @ManyToOne
    @JoinColumn(name = "CODIGO_ESTACION")
    private EstacionSismologica estacionSismologica;

    // Getters y setters
    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }

    public Integer getIdAdquisicion() { return idAdquisicion; }
    public void setIdAdquisicion(Integer idAdquisicion) { this.idAdquisicion = idAdquisicion; }

    public String getNroSerie() { return nroSerie; }
    public void setNroSerie(String nroSerie) { this.nroSerie = nroSerie; }

    public EstacionSismologica getEstacionSismologica() { return estacionSismologica; }
    public void setEstacionSismologica(EstacionSismologica estacionSismologica) { this.estacionSismologica = estacionSismologica; }
}
