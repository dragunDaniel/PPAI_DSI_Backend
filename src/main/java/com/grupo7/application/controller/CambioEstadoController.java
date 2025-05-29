package com.grupo7.application.controller;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// DTOs
import com.grupo7.application.dto.CambioEstadoDTO;

// Gestor Service
import com.grupo7.application.service.CambioEstadoService;

@RestController
@RequestMapping("/api/cambiosDeEstado")
public class CambioEstadoController {

    private final CambioEstadoService cambioEstadoService;

    @Autowired
    public CambioEstadoController(CambioEstadoService cambioEstadoService) {
        this.cambioEstadoService = cambioEstadoService;
    }

    // RegistrarRevisionManual
    @GetMapping("/cambiEstado")
    public ResponseEntity<List<CambioEstadoDTO>> obtenerCambiosDeEstado() {
        List<CambioEstadoDTO> cambiosDeEstado = cambioEstadoService.obtenerTodosDTO();
        return ResponseEntity.ok(cambiosDeEstado);
    }
        
}