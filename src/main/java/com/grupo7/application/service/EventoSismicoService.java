package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Entities
import com.grupo7.application.entity.EventoSismico;

// Repositories
import com.grupo7.application.repository.EventoSismicoRepository;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;

// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;

@Service
public class EventoSismicoService {

    private final EventoSismicoRepository eventoSismicoRepository;
    private final eventoSismicoMapper eventoSismicoMapper;

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.eventoSismicoMapper = eventoSismicoMapper;
    }

    public List<eventoSismicoDTO> obtenerTodosDTO() {
        List<eventoSismico> entidades = eventoSismicoRepository.findAll();
        return entidades.stream()
                        .map(eventoSismicoMapper::toDTO)
                        .toList();
    }
    
    public eventoSismicoDTO crearDesdeDTO(eventoSismicoDTO dto) {
        eventoSismico entidad = eventoSismicoMapper.toEntity(dto);
        eventoSismico guardado = eventoSismicoRepository.save(entidad);
        return eventoSismicoMapper.toDTO(guardado);
    }
    
    public eventoSismicoDTO actualizarDesdeDTO(Long id, eventoSismicoDTO dto) {
        return eventoSismicoRepository.findById(id)
            .map(existing -> {
                eventoSismico entidadActualizada = eventoSismicoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                eventoSismico guardado = eventoSismicoRepository.save(entidadActualizada);
                return eventoSismicoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}    