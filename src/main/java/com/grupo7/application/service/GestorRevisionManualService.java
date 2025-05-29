package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

// Entidades
import com.grupo7.application.entity.EventoSismico;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.EventoSismicoBuscadoDTO;
import com.grupo7.application.dto.SerieTemporalDetalleDTO; // Se mantiene si se usa para otros fines o como parte de un DTO anidado
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.CambioEstadoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO; // Importado para el tipo de retorno
import com.grupo7.application.dto.EmpleadoDTO;
import com.grupo7.application.dto.UsuarioDTO;

// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;
import com.grupo7.application.mapper.EventoSismicoBuscadoMapper; // Make sure this import is present

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios y servicios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;
    private final EventoSismicoService eventoSismicoService;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final EstadoService estadoService;
    private final GestorGenerarSismogramaService gestorGenerarSismogramaService;
    private final UsuarioService usuarioService;
    private final CambioEstadoService cambioEstadoService; // Added previously
    private final EventoSismicoBuscadoMapper eventoSismicoBuscadoMapper; // <--- This field needs to be initialized

    // Evento Sismico Seleccionado
    private EventoSismicoDTO eventoSismicoSeleccionadoDTO;

    @Autowired
    public GestorRevisionManualService(
            TipoDeDatoService tipoDeDatoService,
            EventoSismicoService eventoSismicoService,
            EventoSismicoMapper eventoSismicoMapper,
            EstadoService estadoService,
            GestorGenerarSismogramaService gestorGenerarSismogramaService,
            UsuarioService usuarioService,
            CambioEstadoService cambioEstadoService, // Added previously
            EventoSismicoBuscadoMapper eventoSismicoBuscadoMapper) { // <--- ADD THIS PARAMETER
        this.tipoDeDatoService = tipoDeDatoService;
        this.eventoSismicoService = eventoSismicoService;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.estadoService = estadoService;
        this.gestorGenerarSismogramaService = gestorGenerarSismogramaService;
        this.usuarioService = usuarioService;
        this.cambioEstadoService = cambioEstadoService;
        this.eventoSismicoBuscadoMapper = eventoSismicoBuscadoMapper; // <--- ASSIGN IT HERE
        this.eventoSismicoSeleccionadoDTO = null; // Inicializando el evento sismico seleccionado
    }

    // Registrar Revisión Manual - Flujo
    public List<EventoSismicoDTO> registrarRevisionManual() {
        // Filtrar Eventos Sismicos No Revisados Desordenados por Fecha y Hora de Ocurrencia
        List<EventoSismicoBuscadoDTO> eventosSismicosFiltrados = buscarEventosSismicosNoRevisados();

        // Obtener datos principales
        List<EventoSismicoDTO> datosPrincipales = eventoSismicoService.obtenerDatosPrincipales(eventosSismicosFiltrados);

        // Ordenar dator principales por fecha y hora de ocurrencia
        datosPrincipales = ordenarPorFechaDeOcurrencia(datosPrincipales);

        return datosPrincipales;
    }

    public List<EventoSismicoBuscadoDTO> buscarEventosSismicosNoRevisados() {
        List<EventoSismicoBuscadoDTO> eventosSismicosFiltrados = new ArrayList<>();

        for (EventoSismico eventoSismico : eventoSismicoService.obtenerTodosNoDTO()) {
            CambioEstadoDTO cambioEstadoActual = cambioEstadoService.obtenerCambioEstadoActual(eventoSismico);

            if (cambioEstadoActual == null || cambioEstadoActual.getEstado() == null) {
                continue;
            }

            if (eventoSismicoService.esAutoDetectadoOPendienteDeRevision(cambioEstadoActual.getEstado().getId())) {
                // This is the line that was throwing the NullPointerException
                eventosSismicosFiltrados.add(eventoSismicoBuscadoMapper.toBuscadoDTO(eventoSismico));
            }
        }
        return eventosSismicosFiltrados;
    }

    // Ordenar datos principales de eventos sismicos por fecha de ocurrencia
    public List<EventoSismicoDTO> ordenarPorFechaDeOcurrencia(List<EventoSismicoDTO> datosPrincipalesDTO) {
        // Ordenando por Fecha y Hora de Ocurrencia
        datosPrincipalesDTO.sort((evento1, evento2) ->
                evento1.getFechaHoraOcurrencia().compareTo(evento2.getFechaHoraOcurrencia())
        );
        // retornar datos principales ordenados
        return datosPrincipalesDTO;
    }

    public DatosRegistradosDTO tomarEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        this.eventoSismicoSeleccionadoDTO = eventoSismicoSeleccionadoDTO;

        bloquearEventoSismicoSeleccionado(eventoSismicoSeleccionadoDTO);

        DatosRegistradosDTO datosRegistrados = buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);

        List<SerieTemporalDetalleDTO> seriesTemporalesDetallesDTOs = datosRegistrados.getSeriesTemporalesConDetalles();
        ordenarSeriesTemporalesPorCodigoEstacion(seriesTemporalesDetallesDTOs);
        datosRegistrados.setSeriesTemporalesConDetalles(seriesTemporalesDetallesDTOs);

        gestorGenerarSismogramaService.generarSismograma();

        return datosRegistrados;
    }

    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    public void bloquearEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        EstadoDTO estadoBloqueadoDTO = null; // Inicializando el estado buscado en null

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

        EmpleadoDTO empleadoDTO = usuarioService.obtenerEmpleadoActual();
        eventoSismicoService.bloquearPorRevision(this.eventoSismicoSeleccionadoDTO, empleadoDTO, getFechaHoraActual());
    }

    public DatosRegistradosDTO buscarDatosRegistrados(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        DatosRegistradosDTO datosRegistradosDTO = eventoSismicoService.buscarDatosRegistrados(eventoSismicoSeleccionadoDTO);
        return datosRegistradosDTO;
    }

    private void ordenarSeriesTemporalesPorCodigoEstacion(List<SerieTemporalDetalleDTO> seriesTemporalesDetalleDTOs) {
        if (seriesTemporalesDetalleDTOs != null) {
            seriesTemporalesDetalleDTOs.sort(Comparator.comparing(SerieTemporalDetalleDTO::getCodigoEstacion,
                    Comparator.nullsLast(String::compareTo)));
        }
    }

    // Rechazar evento sismico selecciondao
    public boolean rechazarEventoSismicoSeleccionado() {
        if (!validarDatosSismicos()) {
            return false;
        }
        rechazarEventoSismico();
        return finCU();
    }

    // Confirmación de evento sismico seleccionado
    public boolean confirmarEventoSismicoSeleccionado() {
        if (!validarDatosSismicos()) {
            return false;
        }
        // Actualizar el estado del evento sismico seleccionado a confirmado
        confirmarEventoSismico();
        // llamar a fin de caso de uso
        return finCU();
    }

    // Confirmar evento sismico seleccionado
    public boolean confirmarEventoSismico() {
        // Verificar si el evento sismico ya está en estado Confirmado
        if (validarEstadoActual(this.eventoSismicoSeleccionadoDTO, "Confirmado")) {
            return false;
        }
        // Confirmar el evento sismico seleccionado
        EmpleadoDTO empleadoDTO = usuarioService.obtenerEmpleadoActual();
        eventoSismicoService.confirmar(this.eventoSismicoSeleccionadoDTO, empleadoDTO, getFechaHoraActual());

        // El cambio a estado Confirmado fue realizado con éxito
        return true;
    }

    public boolean rechazarEventoSismico() {
        if (validarEstadoActual(this.eventoSismicoSeleccionadoDTO, "Rechazado")) {
            return false;
        }
        EmpleadoDTO empleadoDTO = usuarioService.obtenerEmpleadoActual();
        eventoSismicoService.rechazarEventoSismico(this.eventoSismicoSeleccionadoDTO, empleadoDTO, getFechaHoraActual());
        return true;
    }

    // Derivar a experto el evento sismico seleccionado
    public boolean derivarAExpertoEventoSismicoSeleccionado() {
        // Verificar si el evento sismico ya está en estado DerivadoAExperto
        if (validarEstadoActual(this.eventoSismicoSeleccionadoDTO, "DerivadoAExperto")) {
            return false;
        }
        // Derivar al experto el evento sismico seleccionado
        EmpleadoDTO empleadoDTO = usuarioService.obtenerEmpleadoActual();
        eventoSismicoService.derivarAExperto(this.eventoSismicoSeleccionadoDTO, empleadoDTO, getFechaHoraActual());
        // El cambio a estado DerivadoAExperto fue realizado con éxito
        return true;
    }

    // Validar Datos Registrados para el Evento Sismico Seleccionado
    private boolean validarDatosSismicos() {
        // Obteniendo la entidad del evento sismico seleccionado
        EventoSismico eventoSismicoSeleccionadoNoDTO = eventoSismicoService.obtenerEntidadDesdeDTO(this.eventoSismicoSeleccionadoDTO);

        // Validando los datos del evento sismico
        if ( eventoSismicoSeleccionadoNoDTO.getAlcanceSismo() == null ||
             eventoSismicoSeleccionadoNoDTO.getOrigenGeneracion() == null ||
             eventoSismicoSeleccionadoNoDTO.getValorMagnitud() == null) {
            // Los datos no son válidos
            return false;
        }
        // Los datos son válidos para el evento sismico seleccionado
        return true;
    }

    // Verificar si el estado actual de un evento sismico es uno determinado
    public boolean validarEstadoActual(EventoSismicoDTO eventoSismicoSeleccionadoDTO, String estado) {
        // Obtener estado actual de evento sismico
        if ((eventoSismicoService.obtenerEntidadDesdeDTO(eventoSismicoSeleccionadoDTO).getEstadoActual().getNombreEstado()).equals(estado)) {
            return true;
        }
        // El evento sismico no se encuentra en el estado buscado
        return false;
    }

    public boolean finCU() {
        return true;
    }
}