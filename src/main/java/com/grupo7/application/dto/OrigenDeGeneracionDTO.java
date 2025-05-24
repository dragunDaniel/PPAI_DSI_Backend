package com.grupo7.application.dto;

public class OrigenDeGeneracionDTO {

    private Long id;
    private String descripcion;
    private String nombre;

    public OrigenDeGeneracionDTO() {
    }

    public OrigenDeGeneracionDTO(Long id, String descripcion, String nombre) {
        this.id = id;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
