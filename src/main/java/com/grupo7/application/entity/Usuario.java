package com.grupo7.application.entity;

// Dependencies
import jakarta.persistence.*;

// Entidades

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idUsuario;

    public String nombreUsuario;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    public Empleado empleado;

    private String contraseña;

    // Getters y Setters
    private String getContraseña() {
        return contraseña;
    }

    private void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

}
