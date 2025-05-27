package com.grupo7.application.service;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO;

// Servicios de Entidades
import com.grupo7.application.service.TipoDeDatoService;
import com.grupo7.application.service.EventoSismicoService;
import com.grupo7.application.service.EstadoService;

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;
    private final EventoSismicoService eventoSismicoService;
    private final EstadoService estadoService;

    @Autowired
    public GestorRevisionManualService (TipoDeDatoService tipoDeDatoService, EventoSismicoService eventoSismicoService, EstadoService estadoService) {
        this.tipoDeDatoService = tipoDeDatoService;
        this.eventoSismicoService = eventoSismicoService;
        this.estadoService = estadoService;
    }

    // Registrar Revisión Manual - Flujo
    public List<EventoSismicoDTO> registrarRevisionManual() {
        return buscarEventosSismicosNoRevisados();
    }

    // Buscar todos los eventos sismicos no revisados 
    public List<EventoSismicoDTO> buscarEventosSismicosNoRevisados() {

        List<EventoSismicoDTO> eventosSismicosFiltrados = eventoSismicoService.esAutoDetectadoOPendienteDeRevision();
        return eventosSismicosFiltrados;
        
    }

    // Ordenar datos principales de eventos sismicos por fecha de ocurrencia
    public List<EventoSismicoDTO> ordenarPorFechaDeOcurrencia() {
        List<EventoSismicoDTO> eventosSismicosFiltradosDTO = buscarEventosSismicosNoRevisados();
    
        eventosSismicosFiltradosDTO.sort((evento1, evento2) -> 
            evento1.getFechaHoraOcurrencia().compareTo(evento2.getFechaHoraOcurrencia())
        );
    
        return eventosSismicosFiltradosDTO;
    }

    // Tomar Seleccion Evento Sismico - Flujo
    public DatosRegistradosDTO tomarEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
    
        // Bloquear EventoSismicoSeleccionado
        bloquearEventoSismicoSeleccionado(eventoSismicoSeleccionadoDTO);

        // Buscar Datos Registrados
        DatosRegistradosDTO datosRegistradosDTO = buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);

        System.out.println("DATOS REGISTTRADOS EXISTE Y ES: " + datosRegistradosDTO.toString());

        return datosRegistradosDTO;
    }

    // Obtener hora actual del sistema
    public LocalDateTime obtenerHoraActual() {
        return LocalDateTime.now();
    }    

    // Bloquear Evento Sismico Seleccionado
    public void bloquearEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        
        EstadoDTO estadoBloqueadoDTO = new EstadoDTO();

        // Obteniendo el estado bloqueado
        for (EstadoDTO estadoDTO : estadoService.obtenerTodosDTO()) {
            if (estadoDTO.sosBloqueadoEnRevision()) {
                estadoBloqueadoDTO = estadoDTO;
                break;
            }
        }

        // Bloquear es estado del evento sismico por revision
        eventoSismicoService.bloquearPorRevision(eventoSismicoSeleccionadoDTO, obtenerHoraActual(), estadoBloqueadoDTO);   
    }

    public DatosRegistradosDTO buscarDatosRegistrados(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        
        DatosRegistradosDTO datosRegistradosDTO = eventoSismicoService.buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);

        return datosRegistradosDTO;
    }

}