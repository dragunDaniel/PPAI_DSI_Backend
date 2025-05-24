package com.grupo7.application.controller;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import javax.annotation.processing.Generated;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;

// Gestor Service
import com.grupo7.application.service.GestorRevisionManualService;

@RestController
@RequestMapping("/api/gestor-revision-manual")
public class GestorRevisionManualController {

    private final GestorRevisionManualService gestorRevisionManualService;

    @Autowired
    public GestorRevisionManualController(GestorRevisionManualService gestorRevisionManualService) {
        this.gestorRevisionManualService = gestorRevisionManualService;
    }

    // RegistrarRevisionManual
    @GetMapping("/registrarRevisionManual")
    public ResponseEntity<List<EventoSismicoDTO>> registrarRevisionManual() {
        List<EventoSismicoDTO> eventosSismicos = gestorRevisionManualService.registrarRevisionManual();
        return ResponseEntity.ok(eventosSismicos);
    }

    // mostrar todos los tipos de datos
    @GetMapping("/tipoDeDato")
    public ResponseEntity<List<TipoDeDatoDTO>> mostrarTiposDeDato() {
        List<TipoDeDatoDTO> tiposDeDato = gestorRevisionManualService.mostrarTiposDeDato();
        return ResponseEntity.ok(tiposDeDato);
    }

}