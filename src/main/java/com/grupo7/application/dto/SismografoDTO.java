package com.grupo7.application.dto;

public class SismografoDTO {

    private String identificador;
    private Integer idAdquisicion;
    private String nroSerie;
    private String codigoEstacion;

    public SismografoDTO() {
    }

    public SismografoDTO(String identificador, Integer idAdquisicion, String nroSerie, String codigoEstacion) {
        this.identificador = identificador;
        this.idAdquisicion = idAdquisicion;
        this.nroSerie = nroSerie;
        this.codigoEstacion = codigoEstacion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getIdAdquisicion() {
        return idAdquisicion;
    }

    public void setIdAdquisicion(Integer idAdquisicion) {
        this.idAdquisicion = idAdquisicion;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getCodigoEstacion() {
        return codigoEstacion;
    }

    public void setCodigoEstacion(String codigoEstacion) {
        this.codigoEstacion = codigoEstacion;
    }
}
