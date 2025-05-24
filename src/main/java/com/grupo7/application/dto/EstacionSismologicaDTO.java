package com.grupo7.application.dto;

import java.time.LocalDate;

public class EstacionSismologicaDTO {

    private String codigoEstacion;
    private String documentoCertAdq;
    private LocalDate fechaSolicitudCert;
    private Double latitud;
    private Double longitud;
    private String nombre;
    private String nroCertAdquisicion;

    public EstacionSismologicaDTO() {
    }

    public EstacionSismologicaDTO(String codigoEstacion, String documentoCertAdq, LocalDate fechaSolicitudCert,
                                  Double latitud, Double longitud, String nombre, String nroCertAdquisicion) {
        this.codigoEstacion = codigoEstacion;
        this.documentoCertAdq = documentoCertAdq;
        this.fechaSolicitudCert = fechaSolicitudCert;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nroCertAdquisicion = nroCertAdquisicion;
    }

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }

    public String getDocumentoCertAdq() {
        return documentoCertAdq;
    }

    public void setDocumentoCertAdq(String documentoCertAdq) {
        this.documentoCertAdq = documentoCertAdq;
    }

    public LocalDate getFechaSolicitudCert() {
        return fechaSolicitudCert;
    }

    public void setFechaSolicitudCert(LocalDate fechaSolicitudCert) {
        this.fechaSolicitudCert = fechaSolicitudCert;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNroCertAdquisicion() {
        return nroCertAdquisicion;
    }

    public void setNroCertAdquisicion(String nroCertAdquisicion) {
        this.nroCertAdquisicion = nroCertAdquisicion;
    }
}
