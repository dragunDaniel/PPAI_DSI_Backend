package com.grupo7.application.dto;

// Entidades
import com.grupo7.application.entity.Empleado;
import com.grupo7.application.entity.Usuario;

public class UsuarioDTO {
    public Long idUsuario;
    public String nombreUsuario;
    public Empleado empleado;

    // Getters y Setters

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
