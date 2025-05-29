package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository, EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    // Obtener el estado BloqueadoEnRevision
    public EstadoDTO obtenerEstadoBloqueado() {
        Estado estado = estadoRepository.findByNombreEstado("BloqueadoEnRevision")
            .orElseThrow(() -> new RuntimeException("Estado Bloqueado no encontrado"));
        return estadoMapper.toDTO(estado);
    }
    
    // Obtener la entidad a través de su id (buscar el objeto persistido)
    public Estado obtenerEntidadPorId(Long id) {
        return estadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
    }    

    // Saber si soy un estado determinado por mi id
    public EstadoDTO obtenerPorId(Long id) {
        return estadoRepository.findById(id)
            .map(estadoMapper::toDTO)
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
    public boolean sosBloqueadoEnRevision(Long id) {
        return obtenerPorId(id).getNombreEstado().equals("BloqueadoEnRevision");
    }

    public List<EstadoDTO> obtenerTodosDTO() {
        List<Estado> entidades = estadoRepository.findAll();
        return entidades.stream()
                        .map(estadoMapper::toDTO)
                        .toList();
    }
    
    public EstadoDTO crearDesdeDTO(EstadoDTO dto) {
        Estado entidad = estadoMapper.toEntity(dto);
        Estado guardado = estadoRepository.save(entidad);
        return estadoMapper.toDTO(guardado);
    }
    
    public EstadoDTO actualizarDesdeDTO(Long id, EstadoDTO dto) {
        return estadoRepository.findById(id)
            .map(existing -> {
                Estado entidadActualizada = estadoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                Estado guardado = estadoRepository.save(entidadActualizada);
                return estadoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
    }
}    