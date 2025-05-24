package com.grupo7.application.service;

// Dependencias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;

// Servicios de Entidades
import com.grupo7.application.service.TipoDeDatoService;

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;

    @Autowired
    public GestorRevisionManualService (TipoDeDatoService tipoDeDatoService) {
        this.tipoDeDatoService = tipoDeDatoService;
    }

    // Servicios para Registrar Revision Manual
    
    // Buscar todos los eventos sismicos no revisados 
    public List<EventoSismicoDTO> buscarEventosSismicosNoRevisados() {
        return eventoSismicoService.buscarEventosSismicosNoRevisados();
    }

    // mostrar todos los Tipos de Datos
    public List<TipoDeDatoDTO> mostrarTiposDeDato() {
        return tipoDeDatoService.obtenerTodosDTO();
    }

}