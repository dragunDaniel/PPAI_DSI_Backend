package com.grupo7.application.service;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;

// Servicios de Entidades
import com.grupo7.application.service.TipoDeDatoService;
import com.grupo7.application.service.EventoSismicoService;

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;
    private final EventoSismicoService eventoSismicoService;

    @Autowired
    public GestorRevisionManualService (TipoDeDatoService tipoDeDatoService, EventoSismicoService eventoSismicoService) {
        this.tipoDeDatoService = tipoDeDatoService;
        this.eventoSismicoService = eventoSismicoService;
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
    

    // mostrar todos los Tipos de Datos
    public List<TipoDeDatoDTO> mostrarTiposDeDato() {
        return tipoDeDatoService.obtenerTodosDTO();
    }

}