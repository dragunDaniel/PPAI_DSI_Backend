package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.SerieTemporalDetalleDTO; // Se mantiene si se usa para otros fines o como parte de un DTO anidado
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO; // Importado para el tipo de retorno

// Servicios de Entidades
import com.grupo7.application.service.TipoDeDatoService;
import com.grupo7.application.service.EventoSismicoService;
import com.grupo7.application.service.EstadoService;

// Otros Gestores
import com.grupo7.application.service.GestorGenerarSismogramaService;

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;
    private final EventoSismicoService eventoSismicoService;
    private final EstadoService estadoService;
    private final GestorGenerarSismogramaService gestorGenerarSismogramaService;

    @Autowired
    public GestorRevisionManualService (TipoDeDatoService tipoDeDatoService, EventoSismicoService eventoSismicoService, EstadoService estadoService, GestorGenerarSismogramaService gestorGenerarSismogramaService) {
        this.tipoDeDatoService = tipoDeDatoService;
        this.eventoSismicoService = eventoSismicoService;
        this.estadoService = estadoService;
        this.gestorGenerarSismogramaService = gestorGenerarSismogramaService;
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

    /**
     * Tomar la selección de un Evento Sísmico.
     * Bloquea el evento y luego busca sus datos registrados en una estructura jerárquica.
     * @param eventoSismicoSeleccionadoDTO El DTO del evento sísmico seleccionado.
     * @return Un DatosRegistradosDTO con la información detallada y jerárquica.
     */
    public DatosRegistradosDTO tomarEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
    
        // Bloquear EventoSismicoSeleccionado
        bloquearEventoSismicoSeleccionado(eventoSismicoSeleccionadoDTO);

        // Buscar Datos Registrados (ahora retorna DatosRegistradosDTO)
        DatosRegistradosDTO datosRegistrados = buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);

        // Categorizas datos registrados por estación sismica 
        List<SerieTemporalDetalleDTO> seriesTemporalesDetallesDTOs = datosRegistrados.getSeriesTemporalesConDetalles();
        ordenarSeriesTemporalesPorCodigoEstacion(seriesTemporalesDetallesDTOs);
        datosRegistrados.setSeriesTemporalesConDetalles(seriesTemporalesDetallesDTOs);

        // Llamar al caso de uso Generar Sismograma
        gestorGenerarSismogramaService.generarSismograma();

        // Devolver los datos registrados para el sismografo seleccionado
        return datosRegistrados;
    }

    // Obtener hora actual del sistema
    public LocalDateTime obtenerHoraActual() {
        return LocalDateTime.now();
    }    

    /**
     * Bloquea el estado de un evento sísmico seleccionado a "BloqueadoEnRevision".
     * @param eventoSismicoSeleccionadoDTO El DTO del evento sísmico a bloquear.
     */
    public void bloquearEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        
        EstadoDTO estadoBloqueadoDTO = null; // Initialize to null

        // Se busca el estado "BloqueadoEnRevision"
        for (EstadoDTO estadoDTO : estadoService.obtenerTodosDTO()) {
            if (estadoDTO.sosBloqueadoEnRevision()) {
                estadoBloqueadoDTO = estadoDTO;
                break;
            }
        }
        
        if (estadoBloqueadoDTO == null) {
            throw new RuntimeException("Estado 'BloqueadoEnRevision' no encontrado en la base de datos.");
        }

        // Bloquear el estado del evento sismico por revision
        eventoSismicoService.bloquearPorRevision(eventoSismicoSeleccionadoDTO, obtenerHoraActual(), estadoBloqueadoDTO);    
    }

    /**
     * Busca los datos registrados para un evento sísmico, y los transforma en un DatosRegistradosDTO
     * que contiene la información jerárquica de las series temporales y sus detalles.
     * @param eventoSismicoSeleccionadoDTO El DTO del evento sísmico seleccionado.
     * @return Un DatosRegistradosDTO con la información detallada y jerárquica.
     */
    public DatosRegistradosDTO buscarDatosRegistrados(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        // Se llama a EventoSismicoService para obtener el DTO jerárquico completo (DatosRegistradosDTO)
        DatosRegistradosDTO datosRegistradosDTO = eventoSismicoService.buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);
        
        // El DatosRegistradosDTO ya contiene la lista de SerieTemporalDetalleDTOs,
        // así que se retorna directamente.
        return datosRegistradosDTO;
    }

    /**
     * Orders a list of SerieTemporalDetalleDTO by the 'codigoEstacion' field.
     * @param seriesTemporalesDetalleDTOs The list to be sorted.
     */
    private void ordenarSeriesTemporalesPorCodigoEstacion(List<SerieTemporalDetalleDTO> seriesTemporalesDetalleDTOs) {
        if (seriesTemporalesDetalleDTOs != null) {
            seriesTemporalesDetalleDTOs.sort(Comparator.comparing(SerieTemporalDetalleDTO::getCodigoEstacion,
                                                      Comparator.nullsLast(String::compareTo)));
        }
    }

}
