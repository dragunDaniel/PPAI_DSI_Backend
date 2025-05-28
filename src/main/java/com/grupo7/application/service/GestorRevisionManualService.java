package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;

// Entidades
import com.grupo7.application.entity.EventoSismico;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.SerieTemporalDetalleDTO; // Se mantiene si se usa para otros fines o como parte de un DTO anidado
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO; // Importado para el tipo de retorno
import com.grupo7.application.dto.EmpleadoDTO;
import com.grupo7.application.dto.UsuarioDTO;

// Mappers 
import com.grupo7.application.mapper.EventoSismicoMapper;

// Servicios de Entidades
import com.grupo7.application.service.TipoDeDatoService;
import com.grupo7.application.service.EventoSismicoService;
import com.grupo7.application.service.EstadoService;
import com.grupo7.application.service.UsuarioService;

// Otros Gestores
import com.grupo7.application.service.GestorGenerarSismogramaService;

@Service
public class GestorRevisionManualService {

    // Definiendo los repositorios y servicios necesarios para mapear las entidades a la base de datsos
    private final TipoDeDatoService tipoDeDatoService;
    private final EventoSismicoService eventoSismicoService;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final EstadoService estadoService;
    private final GestorGenerarSismogramaService gestorGenerarSismogramaService;
    private final UsuarioService usuarioService;

    // Evento Sismico Seleccionado
    private EventoSismicoDTO eventoSismicoSeleccionadoDTO;

    @Autowired
    public GestorRevisionManualService (TipoDeDatoService tipoDeDatoService, EventoSismicoService eventoSismicoService, EventoSismicoMapper eventoSismicoMapper, EstadoService estadoService, GestorGenerarSismogramaService gestorGenerarSismogramaService, UsuarioService usuarioService) {
        this.tipoDeDatoService = tipoDeDatoService;
        this.eventoSismicoService = eventoSismicoService;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.estadoService = estadoService;
        this.gestorGenerarSismogramaService = gestorGenerarSismogramaService;
        this.usuarioService = usuarioService;
        this.eventoSismicoSeleccionadoDTO = null; // Inicializando el evento sismico seleccionado 
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
    
    public DatosRegistradosDTO tomarEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
    
        // Guardar evento sismico seleccionado en el gestor
        this.eventoSismicoSeleccionadoDTO = eventoSismicoSeleccionadoDTO;

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

    public void bloquearEventoSismicoSeleccionado(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        
        EstadoDTO estadoBloqueadoDTO = null; // Inicializando el estado buscado en null

        // Se busca el estado "BloqueadoEnRevision"
        for (EstadoDTO estadoDTO : estadoService.obtenerTodosDTO()) {
            if (estadoDTO.sosBloqueadoEnRevision()) {
                estadoBloqueadoDTO = estadoDTO;
                break;
            }
        }
        
        // Revisando que efectivamente el estado bloqueado haya sido encontrado en la base de datos
        if (estadoBloqueadoDTO == null) {
            throw new RuntimeException("Estado 'BloqueadoEnRevision' no encontrado en la base de datos.");
        }

        // Bloquear el estado del evento sismico por revision
        eventoSismicoService.bloquearPorRevision(eventoSismicoSeleccionadoDTO, obtenerHoraActual(), estadoBloqueadoDTO);    
    }

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

    // 
    public boolean rechazarEventoSismicoSeleccionado() {
        
        // Validar Datos Registrados para el Evento Sismico Seleccionado
        if (validarDatosSismicos() == false) {
            return false;
        }

        // Actualizar el estado del evento sismico seleccionado a rechazado
        rechazarEventoSismico();

        
        // llamar a fin de caso de uso
        return finCU();

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

    public void rechazarEventoSismico() {
        
        EstadoDTO estadoRechazadoDTO = null; // Initialize to null

        // Se busca el estado "Rechazado"
        for (EstadoDTO estadoDTO : estadoService.obtenerTodosDTO()) {
            if (estadoDTO.sosRechazado()) {
                estadoRechazadoDTO = estadoDTO;
                break;
            }
        }

        // Revisando que efectivamente el estado rechazado haya sido encontrado en la base de datos
        if (estadoRechazadoDTO == null) {
            throw new RuntimeException("Estado 'Rechazado' no encontrado en la base de datos.");
        }

        // Obteniendo al usaurio logueado con id 1
        UsuarioDTO usuarioLogueadoDTO = usuarioService.obtenerUsuarioDTO(1L);

        // Obteniendo al responsable de inspeccion (empleado logueado como usuario responsable de este cambio de esatdo)
        EmpleadoDTO responsableDeInspeccionDTO = usuarioService.obtenerEmpleado(usuarioLogueadoDTO.getIdUsuario());

        System.out.println("d");

        // Rechazar el evento sismico seleccionado 
        eventoSismicoService.rechazar(this.eventoSismicoSeleccionadoDTO, obtenerHoraActual(), estadoRechazadoDTO, responsableDeInspeccionDTO);

    }

    public boolean finCU() {
        return true; 
    }

}
