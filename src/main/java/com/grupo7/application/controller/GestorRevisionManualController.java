package com.grupo7.application.controller;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO;
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

    // tomarEventoSismicoSeleccionado
    @PostMapping("/tomarEventoSismicoSeleccionado")
    public ResponseEntity<DatosRegistradosDTO> tomarEventoSismicoSeleccionado(@RequestBody EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        DatosRegistradosDTO datosRegistradosDTO = gestorRevisionManualService.tomarEventoSismicoSeleccionado(eventoSismicoSeleccionadoDTO);
        return ResponseEntity.ok(datosRegistradosDTO);
    }

    // tomarNoVisualizacion
    @GetMapping("/tomarNoVisualizacion")
    public ResponseEntity tomarNoVisualizacion() {
        return ResponseEntity.ok().build();
    }

    // tomarRechazoModificacion
    @GetMapping("/tomarRechazoModificacion")
    public ResponseEntity tomarRechazoModificacion() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirmarEventoSismicoSeleccionado")
    public ResponseEntity<Boolean> confirmarEventoSismicoSeleccionado() {
        boolean resultado = gestorRevisionManualService.confirmarEventoSismicoSeleccionado();
        return ResponseEntity.ok(resultado);
    }

    // rechazarEvento
    @GetMapping("/rechazarEventoSismicoSeleccionado")
    public ResponseEntity<Boolean> rechazarEventoSismicoSeleccionado() {
        boolean resultado = gestorRevisionManualService.rechazarEventoSismicoSeleccionado();
        return ResponseEntity.ok(resultado);
    }
    
}