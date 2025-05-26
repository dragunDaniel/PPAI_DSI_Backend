package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Entities
import com.grupo7.application.entity.CambioEstado;
import com.grupo7.application.entity.EventoSismico;

// Repositories
import com.grupo7.application.repository.CambioEstadoRepository;

// DTOs
import com.grupo7.application.dto.CambioEstadoDTO;

// Mappers
import com.grupo7.application.mapper.CambioEstadoMapper;

// Services 
import com.grupo7.application.service.EstadoService;

@Service
public class CambioEstadoService {

    private final CambioEstadoRepository cambioEstadoRepository;
    private final CambioEstadoMapper cambioEstadoMapper;
    private final EstadoService estadoService;

    @Autowired
    public CambioEstadoService(CambioEstadoRepository cambioEstadoRepository, CambioEstadoMapper cambioEstadoMapper, EstadoService estadoService) {
        this.cambioEstadoRepository = cambioEstadoRepository;
        this.cambioEstadoMapper = cambioEstadoMapper;
        this.estadoService = estadoService;
    }

    // Obtener el cambio de estado actual para un evento sismico
    public CambioEstadoDTO obtenerCambioEstadoActual(EventoSismico eventoSismico) {
        Optional<CambioEstado> cambioEstadoOpt = cambioEstadoRepository.findByEventoSismicoAndFechaHoraFinIsNull(eventoSismico);
        
        System.out.println("el cambio de estado es: " + cambioEstadoOpt);

        if (cambioEstadoOpt.isPresent()) {
            return cambioEstadoMapper.toDTO(cambioEstadoOpt.get());
        } else {
            // No hay cambio de estado actual para ese evento
            return null;
        }
    }
    
    // Saber si el estado actual es AutoDetectado
    public boolean sosAutoDetectado(Long idCambioEstado) {
        return estadoService.sosAutoDetectado(idCambioEstado);

    }
    
    // Saber si el estado actual es PendienteDeRevision
    public boolean sosPendienteDeRevision(Long idCambioEstado) {
        return estadoService.sosPendienteDeRevision(idCambioEstado);
    }

    public List<CambioEstadoDTO> obtenerTodosDTO() {
        List<CambioEstado> entidades = cambioEstadoRepository.findAll();
        return entidades.stream()
                        .map(cambioEstadoMapper::toDTO)
                        .toList();
    }
    
    public CambioEstadoDTO crearDesdeDTO(CambioEstadoDTO dto) {
        CambioEstado entidad = cambioEstadoMapper.toEntity(dto);
        CambioEstado guardado = cambioEstadoRepository.save(entidad);
        return cambioEstadoMapper.toDTO(guardado);
    }
    
    public CambioEstadoDTO actualizarDesdeDTO(Long id, CambioEstadoDTO dto) {
        return cambioEstadoRepository.findById(id)
            .map(existing -> {
                CambioEstado entidadActualizada = cambioEstadoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                CambioEstado guardado = cambioEstadoRepository.save(entidadActualizada);
                return cambioEstadoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("CambioEstado no encontrado con id: " + id));
    }
}    