package com.grupo7.application.dto;

// Dependencies
import java.util.List;

// Entidades
import com.grupo7.application.entity.DetalleMuestraSismica;

public class DatosRegistradosDTO {
    private String alcanceSismoNombre;
    private String clasificacionSismoNombre;
    private String origenGeneracionNombre;
    private List<DetalleMuestraSismica> datosValidos;

    // Constructor
    public DatosRegistradosDTO(String alcance, String clasificacion, String origen, List<DetalleMuestraSismica> datos) {
        this.alcanceSismoNombre = alcance;
        this.clasificacionSismoNombre = clasificacion;
        this.origenGeneracionNombre = origen;
        this.datosValidos = datos;
    }

    // Getters y setters
    public String getAlcanceSismoNombre() { return alcanceSismoNombre; }
    public String getClasificacionSismoNombre() { return clasificacionSismoNombre; }
    public String getOrigenGeneracionNombre() { return origenGeneracionNombre; }
    public List<DetalleMuestraSismica> getDatosValidos() { return datosValidos; }
}
