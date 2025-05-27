package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

// Entities
import com.grupo7.application.entity.TipoDeDato;

// Repositories
import com.grupo7.application.repository.TipoDeDatoRepository;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;

// Mappers
import com.grupo7.application.mapper.TipoDeDatoMapper;

@Service
public class TipoDeDatoService {

    private final TipoDeDatoRepository tipoDeDatoRepository;
    private final TipoDeDatoMapper tipoDeDatoMapper;

    @Autowired
    public TipoDeDatoService(TipoDeDatoRepository tipoDeDatoRepository, TipoDeDatoMapper tipoDeDatoMapper) {
        this.tipoDeDatoRepository = tipoDeDatoRepository;
        this.tipoDeDatoMapper = tipoDeDatoMapper;
    }

    public TipoDeDatoDTO obtenerPorId(Long id) {
        Optional<TipoDeDato> entidad = tipoDeDatoRepository.findById(id);
        return entidad.map(tipoDeDatoMapper::toDTO).orElse(null);
    }

    public List<TipoDeDatoDTO> obtenerTodosDTO() {
        List<TipoDeDato> entidades = tipoDeDatoRepository.findAll();
        return entidades.stream()
                        .map(tipoDeDatoMapper::toDTO)
                        .toList();
    }
    
    public TipoDeDatoDTO crearDesdeDTO(TipoDeDatoDTO dto) {
        TipoDeDato entidad = tipoDeDatoMapper.toEntity(dto);
        TipoDeDato guardado = tipoDeDatoRepository.save(entidad);
        return tipoDeDatoMapper.toDTO(guardado);
    }
    
    public TipoDeDatoDTO actualizarDesdeDTO(Long id, TipoDeDatoDTO dto) {
        return tipoDeDatoRepository.findById(id)
            .map(existing -> {
                TipoDeDato entidadActualizada = tipoDeDatoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                TipoDeDato guardado = tipoDeDatoRepository.save(entidadActualizada);
                return tipoDeDatoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("TipoDeDato no encontrado con id: " + id));
    }
}    