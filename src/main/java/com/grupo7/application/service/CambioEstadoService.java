package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar para control transaccional
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para operaciones de stream

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

@Service
public class CambioEstadoService {

    private final CambioEstadoRepository cambioEstadoRepository;
    private final CambioEstadoMapper cambioEstadoMapper;
    private final EstadoService estadoService; // Se asume que EstadoService es necesario aquí

    @Autowired
    public CambioEstadoService(CambioEstadoRepository cambioEstadoRepository, CambioEstadoMapper cambioEstadoMapper, EstadoService estadoService) {
        this.cambioEstadoRepository = cambioEstadoRepository;
        this.cambioEstadoMapper = cambioEstadoMapper;
        this.estadoService = estadoService;
    }

    @Transactional(readOnly = true) // Este método solo lee datos
    public CambioEstadoDTO obtenerCambioEstadoActual(EventoSismico eventoSismico) {
        Optional<CambioEstado> cambioEstadoOpt = cambioEstadoRepository.findByEventoSismicoAndFechaHoraFinIsNull(eventoSismico);
        
        // Para depuración, se puede usar un logger en lugar de System.out.println
        System.out.println("DEBUG: El cambio de estado encontrado es: " + cambioEstadoOpt.orElse(null));

        if (cambioEstadoOpt.isPresent()) {
            return cambioEstadoMapper.toDTO(cambioEstadoOpt.get());
        } else {
            // No hay cambio de estado actual para ese evento
            return null;
        }
    }

    @Transactional(readOnly = true)
    public boolean sosAutoDetectado(Long idEstado) {
        return estadoService.sosAutoDetectado(idEstado);
    }

    @Transactional(readOnly = true)
    public boolean sosPendienteDeRevision(Long idEstado) {
        return estadoService.sosPendienteDeRevision(idEstado);
    }

    @Transactional(readOnly = true)
    public List<CambioEstadoDTO> obtenerTodosDTO() {
        List<CambioEstado> entidades = cambioEstadoRepository.findAll();
        return entidades.stream()
                        .map(cambioEstadoMapper::toDTO)
                        .collect(Collectors.toList());
    }
    
    @Transactional
    public CambioEstadoDTO crearDesdeDTO(CambioEstadoDTO dto) {
        CambioEstado entidad = cambioEstadoMapper.toEntity(dto);
        CambioEstado guardado = cambioEstadoRepository.save(entidad);
        return cambioEstadoMapper.toDTO(guardado);
    }

    @Transactional
    public CambioEstadoDTO actualizarDesdeDTO(Long id, CambioEstadoDTO dto) {
        return cambioEstadoRepository.findById(id)
            .map(existing -> {
                CambioEstado entidadActualizada = cambioEstadoMapper.toEntity(dto);
                entidadActualizada.setId(id); // Asegúrate de mantener el id para la actualización
                CambioEstado guardado = cambioEstadoRepository.save(entidadActualizada);
                return cambioEstadoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("CambioEstado no encontrado con id: " + id));
    }
}
