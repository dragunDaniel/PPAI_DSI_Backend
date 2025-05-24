package com.grupo7.application.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estacion_sismologica")
public class EstacionSismologica {

    @Id
    @Column(name = "CODIGO_ESTACION")
    private String codigoEstacion;

    @Column(name = "DOCUMENTO_CERT_ADQ")
    private String documentoCertAdq;

    @Column(name = "FECHA_SOLICITUD_CERT")
    private LocalDate fechaSolicitudCert;

    @Column(name = "LATITUD")
    private Double latitud;

    @Column(name = "LONGITUD")
    private Double longitud;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "NRO_CERT_ADQUISICION")
    private String nroCertAdquisicion;

    // Getters y setters
    public String getCodigoEstacion() { return codigoEstacion; }
    public void setCodigoEstacion(String codigoEstacion) { this.codigoEstacion = codigoEstacion; }

    public String getDocumentoCertAdq() { return documentoCertAdq; }
    public void setDocumentoCertAdq(String documentoCertAdq) { this.documentoCertAdq = documentoCertAdq; }

    public LocalDate getFechaSolicitudCert() { return fechaSolicitudCert; }
    public void setFechaSolicitudCert(LocalDate fechaSolicitudCert) { this.fechaSolicitudCert = fechaSolicitudCert; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNroCertAdquisicion() { return nroCertAdquisicion; }
    public void setNroCertAdquisicion(String nroCertAdquisicion) { this.nroCertAdquisicion = nroCertAdquisicion; }
}
