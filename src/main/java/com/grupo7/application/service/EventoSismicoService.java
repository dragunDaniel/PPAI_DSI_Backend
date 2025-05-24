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

// Repositories
import com.grupo7.application.repository.EventoSismicoRepository;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.CambioEstadoDTO;
import com.grupo7.application.dto.EstadoDTO;


// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;

// Services 
import com.grupo7.application.service.CambioEstadoService;
import com.grupo7.application.service.EstadoService;

@Service
public class EventoSismicoService {

    private final EventoSismicoRepository eventoSismicoRepository;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final CambioEstadoService cambioEstadoService;
    private final EstadoService estadoService;

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper, CambioEstadoService cambioEstadoService, EstadoService estadoService) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.cambioEstadoService = cambioEstadoService;
        this.estadoService = estadoService;
    }

    // Buscar todos los Autorealizados o Pendientes de Revision 
    public List<EventoSismicoDTO> esAutoDetectadoOPendienteDeRevision() {
        
        // Lista de Eventos Sismicos Filtrados
        List<EventoSismicoDTO> eventosSismicosFiltradosDTO = new ArrayList<>();

        // Mientras haya eventos sismicos
        for (EventoSismico eventoSismico : obtenerTodosNoDTO()) {
            
            // busco el estado actual para el evento sismico iterado
            CambioEstadoDTO cambioEstadoActual = cambioEstadoService.obtenerCambioEstadoActual(eventoSismico.getId());

            System.err.println("El cambioEstadoActual esta: " + cambioEstadoActual + "para el evento sismico: " + eventoSismico.getId());

            // Evitando llamar a cambios de estado con id nulo
            if (cambioEstadoActual == null || cambioEstadoActual.getEstadoId() == null) {
                continue;
            }

            // Verificando si el estado actual es AutoDetectado o PendienteDeRevision
            if ((cambioEstadoService.sosAutoDetectado(cambioEstadoActual.getEstadoId())) || (cambioEstadoService.sosPendienteDeRevision(cambioEstadoActual.getEstadoId()))) {
                
                // Agregando el evento sismico a la lista de eventos sismicos filtrados
                eventosSismicosFiltradosDTO.add(eventoSismicoMapper.toDTO(eventoSismico));

            } 
        }

        // Retornando los datos principales (DTO) de los eventos sismicos filtrados
        return eventosSismicosFiltradosDTO;
    } 

    // Bloquear un evento sismico especifico 
    public void bloquearPorRevision(Long id, LocalDateTime fechaHoraActual, Long idEstadoBloqueado) {
        
        // Mientras haya cambios de esatdo
        for (CambioEstadoDTO cambioEstado : cambioEstadoService.obtenerTodosDTO()) {
            
            // Si el cambio de estado iterado es del evento sismico seleccionado y es el estado actual
            if (cambioEstado.getEventoSismicoId() != null && cambioEstado.getEventoSismicoId().equals(id) && cambioEstado.esEstadoActual()) {
                
                // Finalizar el estado actual colocando fecha de fin al cambio de estado
                cambioEstado.setFechaHoraFin(fechaHoraActual);
                cambioEstadoService.actualizarDesdeDTO(cambioEstado.getId(), cambioEstado);

                // Instanciando un nuevo Cambio de Estado para este objeto, con el estado bloqueado
                CambioEstadoDTO cambioEstadoBloqueado = new CambioEstadoDTO();
                cambioEstadoBloqueado.setFechaHoraInicio(fechaHoraActual);
                cambioEstadoBloqueado.setEventoSismicoId(id);
                cambioEstadoBloqueado.setEstadoId(idEstadoBloqueado);
                
                // Creación del nuevo Cambio de Estado
                cambioEstadoService.crearDesdeDTO(cambioEstadoBloqueado);
                
                // Terminado el ciclo
                break; 
            }
        }
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