package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Entities
import com.grupo7.application.entity.Estado;

// Repositories
import com.grupo7.application.repository.EstadoRepository;

// DTOs
import com.grupo7.application.dto.EstadoDTO;

// Mappers
import com.grupo7.application.mapper.EstadoMapper;

@Service
public class EstadoService {

    private final EstadoRepository EstadoRepository;
    private final EstadoMapper EstadoMapper;

    @Autowired
    public EstadoService(EstadoRepository EstadoRepository, EstadoMapper EstadoMapper) {
        this.EstadoRepository = EstadoRepository;
        this.EstadoMapper = EstadoMapper;
    }

    // Saber si soy un estado determinado por mi id
    public EstadoDTO obtenerPorId(Long id) {
        return EstadoRepository.findById(id)
            .map(EstadoMapper::toDTO)
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
    }

    // Saber si el estado es AutoDetectado o no
    public boolean sosAutoDetectado(Long id) {
        return obtenerPorId(id).getNombreEstado().equals("AutoDetectado");
    }

    // Saber si el estado es PendienteDeRevision o no
    public boolean sosPendienteDeRevision(Long id) {
        return obtenerPorId(id).getNombreEstado().equals("PendienteDeRevision"); 
    }

    // Saber si el estado es Bloqueado
    public boolean sosBloqueado(Long id) {
        return obtenerPorId(id).getNombreEstado().equals("Bloqueado");
    }

    public List<EstadoDTO> obtenerTodosDTO() {
        List<Estado> entidades = EstadoRepository.findAll();
        return entidades.stream()
                        .map(EstadoMapper::toDTO)
                        .toList();
    }
    
    public EstadoDTO crearDesdeDTO(EstadoDTO dto) {
        Estado entidad = EstadoMapper.toEntity(dto);
        Estado guardado = EstadoRepository.save(entidad);
        return EstadoMapper.toDTO(guardado);
    }
    
    public EstadoDTO actualizarDesdeDTO(Long id, EstadoDTO dto) {
        return EstadoRepository.findById(id)
            .map(existing -> {
                Estado entidadActualizada = EstadoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                Estado guardado = EstadoRepository.save(entidadActualizada);
                return EstadoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
    }
}    