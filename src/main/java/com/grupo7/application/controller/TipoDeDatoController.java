package com.grupo7.application.controller;

import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.service.TipoDeDatoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-de-dato") // URL base para los endpoints
public class TipoDeDatoController {

    private final TipoDeDatoService tipoDeDatoService;

    @Autowired
    public TipoDeDatoController(TipoDeDatoService tipoDeDatoService) {
        this.tipoDeDatoService = tipoDeDatoService;
    }

    // Obtener todos los tipos de dato
    @GetMapping
    public ResponseEntity<List<TipoDeDatoDTO>> obtenerTodos() {
        return ResponseEntity.ok(tipoDeDatoService.obtenerTodosDTO());
    }

    // Crear un nuevo tipo de dato (POST)
    @PostMapping
    public ResponseEntity<TipoDeDatoDTO> crearTipoDeDato(@RequestBody TipoDeDatoDTO dto) {
        TipoDeDatoDTO creado = tipoDeDatoService.crearDesdeDTO(dto);
        return ResponseEntity
            .created(URI.create("/api/tipos-de-dato/" + creado.getId()))
            .body(creado);
    }

    // Actualizar un tipo de dato existente
    @PutMapping("/{id}")
    public ResponseEntity<TipoDeDatoDTO> actualizarTipoDeDato(@PathVariable Long id, @RequestBody TipoDeDatoDTO dto) {
        TipoDeDatoDTO actualizado = tipoDeDatoService.actualizarDesdeDTO(id, dto);
        return ResponseEntity.ok(actualizado);
    }
}
