package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;

// Entities
import com.grupo7.application.entity.EventoSismico;
import com.grupo7.application.entity.SerieTemporal;
import com.grupo7.application.entity.DetalleMuestraSismica;

// Repositories
import com.grupo7.application.repository.EventoSismicoRepository;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.CambioEstadoDTO;
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO;

// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;
import com.grupo7.application.mapper.EstadoMapper;

// Services 
import com.grupo7.application.service.CambioEstadoService;
import com.grupo7.application.service.EstadoService;
import com.grupo7.application.service.SerieTemporalService;

@Service
public class EventoSismicoService {

    private final EventoSismicoRepository eventoSismicoRepository;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final CambioEstadoService cambioEstadoService;
    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;
    private final SerieTemporalService serieTemporalService;

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper, 
        CambioEstadoService cambioEstadoService, EstadoService estadoService, EstadoMapper estadoMapper,
        SerieTemporalService serieTemporalService) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.cambioEstadoService = cambioEstadoService;
        this.estadoService = estadoService;
        this.estadoMapper = estadoMapper;
        this.serieTemporalService = serieTemporalService; 
    }

    // Buscar todos los Autorealizados o Pendientes de Revision 
    public List<EventoSismicoDTO> esAutoDetectadoOPendienteDeRevision() {
        
        // Lista de Eventos Sismicos Filtrados
        List<EventoSismicoDTO> eventosSismicosFiltradosDTO = new ArrayList<>();

        // Mientras haya eventos sismicos
        for (EventoSismico eventoSismico : obtenerTodosNoDTO()) {
            
            // busco el estado actual para el evento sismico iterado
            CambioEstadoDTO cambioEstadoActual = cambioEstadoService.obtenerCambioEstadoActual(eventoSismico);

            // Evitando llamar a cambios de estado con id nulo
            if (cambioEstadoActual == null || cambioEstadoActual.getEstado() == null) {
                continue;
            }

            // Verificando si el estado actual es AutoDetectado o PendienteDeRevision
            if ((cambioEstadoService.sosAutoDetectado(cambioEstadoActual.getEstado().getId())) ||
            (cambioEstadoService.sosPendienteDeRevision(cambioEstadoActual.getEstado().getId()))) {
                
                // Agregando el evento sismico a la lista de eventos sismicos filtrados
                eventosSismicosFiltradosDTO.add(eventoSismicoMapper.toDTO(eventoSismico));

            } 
        }

        // Retornando los datos principales (DTO) de los eventos sismicos filtrados
        return eventosSismicosFiltradosDTO;
    } 

    // Obtener el Evento Sismico Entidad a partir del DTO 
    private EventoSismico obtenerEntidadDesdeDTO(EventoSismicoDTO dto) {
        return eventoSismicoRepository.findById(dto.getId())
            .orElseThrow(() -> new RuntimeException("EventoSismico no encontrado con id: " + dto.getId()));
    }

    // Bloquear un evento sismico especifico 
    public void bloquearPorRevision(EventoSismicoDTO eventoSismicoSeleccionadoDTO, LocalDateTime fechaHoraActual, EstadoDTO estadoBloqueadoDTO) {
    
        // Buscar el cambio de estado actual para el evento seleccionado
        for (CambioEstadoDTO cambioEstadoDTO : cambioEstadoService.obtenerTodosDTO()) {
    
            // Si el cambio de estado pertenece al evento seleccionado y es el actual
            if (cambioEstadoDTO.getEventoSismico() != null &&
                cambioEstadoDTO.getEventoSismico().getId().equals(eventoSismicoSeleccionadoDTO.getId()) &&
                cambioEstadoDTO.esEstadoActual()) {
                
                // Finalizar el estado actual colocando fecha de fin
                cambioEstadoDTO.setFechaHoraFin(fechaHoraActual);
                
                // Actualizar el cambio de estado en la base
                CambioEstadoDTO cambioEstadoGuardado = cambioEstadoService.actualizarDesdeDTO(cambioEstadoDTO.getId(), cambioEstadoDTO);
                System.out.println("El cambio de estado guardado es: " + cambioEstadoGuardado);
    
                // Crear un nuevo cambio de estado con estado "bloqueado"
                CambioEstadoDTO cambioEstadoBloqueado = new CambioEstadoDTO();
                cambioEstadoBloqueado.setFechaHoraInicio(fechaHoraActual);
                cambioEstadoBloqueado.setEventoSismico(obtenerEntidadDesdeDTO(eventoSismicoSeleccionadoDTO));

                // Buscar el Estado persistido desde el repositorio (o servicio) antes de setearlo al nuevo cambioEstado que apunta a Estado Bloqueado
                Long estadoId = estadoBloqueadoDTO.getId();
                System.out.println(" -----__---------> EL ID DEL ESATDO ES: ---> " + estadoId);
                
                // Evitando llamar a cambios de estado con id nulo)
                if (estadoId == null) { 
                    throw new RuntimeException("El DTO de Estado no contiene un ID válido."); 
                }

                // Primero asignar el estado
                cambioEstadoBloqueado.setEstado(estadoService.obtenerEntidadPorId(estadoId));

                // Después imprimir con seguridad
                System.out.println("Estado persistido asignado al cambio de estado: " + cambioEstadoBloqueado.getEstado().getId());

                // Guardar el nuevo cambio de estado
                cambioEstadoService.crearDesdeDTO(cambioEstadoBloqueado);
                
                // Terminado el ciclo
                break;
            }
        }
    }

    // Obtener Datos Registrados
    public DatosRegistradosDTO buscarDatosRegistrados(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        
        // Obteniendo el evento sismico entidad del evento sismico seleccionado
        EventoSismico eventoSismicoEntidad = obtenerEntidadDesdeDTO(eventoSismicoSeleccionadoDTO);

        // Obteniendo los datos registrados del evento sismico seleccionado
        String alcanceSismoNombre = eventoSismicoEntidad.getAlcanceSismo().getNombre();
        String clasificacionSismoNombre = eventoSismicoEntidad.getClasificacionSismo().getNombre();
        String origenGeneracionNombre = eventoSismicoEntidad.getOrigenGeneracion().getNombre();

        // Definiendo la lista de datos buscados
        List<DetalleMuestraSismica> datosValidos = new ArrayList<>();

        // Recorriendo las series temporales del evento sismico seleccionado
        for (SerieTemporal serieTemporal : eventoSismicoEntidad.getSeriesTemporales()) {
            
            // Agregando los datos buscados
            if (serieTemporalService.getDatos(serieTemporal) != null) {
                datosValidos.addAll(serieTemporalService.getDatos(serieTemporal));
            }
        }

        // Retornando los Dator Registrados
        return new DatosRegistradosDTO(alcanceSismoNombre, clasificacionSismoNombre, origenGeneracionNombre, datosValidos);

    }

    public List<EventoSismico> obtenerTodosNoDTO() {
        List<EventoSismico> entidades = eventoSismicoRepository.findAll();
        return entidades;
    }

    public List<EventoSismicoDTO> obtenerTodosDTO() {
        return eventoSismicoRepository.findAll()
            .stream()
            .map(entidad -> eventoSismicoMapper.toDTO(entidad))
            .toList();
    }

    public EventoSismicoDTO crearDesdeDTO(EventoSismicoDTO dto) {
        EventoSismico entidad = eventoSismicoMapper.toEntity(dto);
        EventoSismico guardado = eventoSismicoRepository.save(entidad);
        return eventoSismicoMapper.toDTO(guardado);
    }
    
    public EventoSismicoDTO actualizarDesdeDTO(Long id, EventoSismicoDTO dto) {
        return eventoSismicoRepository.findById(id)
            .map(existing -> {
                EventoSismico entidadActualizada = eventoSismicoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                EventoSismico guardado = eventoSismicoRepository.save(entidadActualizada);
                return eventoSismicoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}    