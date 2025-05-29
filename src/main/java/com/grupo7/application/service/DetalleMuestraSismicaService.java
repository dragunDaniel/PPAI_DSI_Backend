package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional
import java.util.List;
import java.util.ArrayList; // Added for new method if needed

// Entities
import com.grupo7.application.entity.DetalleMuestraSismica;

// Repositories
import com.grupo7.application.repository.DetalleMuestraSismicaRepository;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;

// Mappers
import com.grupo7.application.mapper.TipoDeDatoMapper;
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;

@Service
public class DetalleMuestraSismicaService {

    private final DetalleMuestraSismicaRepository detalleMuestraSismicaRepository;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;
    private final TipoDeDatoMapper tipoDeDatoMapper;
    private final TipoDeDatoService tipoDeDatoService; // Added for completeness in constructor, as it was in original

    @Autowired
    public DetalleMuestraSismicaService(DetalleMuestraSismicaRepository detalleMuestraSismicaRepository, DetalleMuestraSismicaMapper detalleMuestraSismicaMapper,
                                        TipoDeDatoService tipoDeDatoService, TipoDeDatoMapper tipoDeDatoMapper) {
        this.detalleMuestraSismicaRepository = detalleMuestraSismicaRepository;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
        this.tipoDeDatoService = tipoDeDatoService; // Assign it
        this.tipoDeDatoMapper = tipoDeDatoMapper;
    }
    
    @Transactional(readOnly = true)
    public DetalleMuestraSismicaDTO getDatos(DetalleMuestraSismica detalleMuestraSismicaEntity) {
        if (detalleMuestraSismicaEntity == null) {
            return null;
        }

        // Directly get the TipoDeDato from the entity
        TipoDeDatoDTO tipoDTO = tipoDeDatoMapper.toDTO(detalleMuestraSismicaEntity.getTipoDeDato());

        String denominacion = null;
        if (tipoDTO.esTuDenominacion("Velocidad")) {
            denominacion = "Velocidad";
        } else if (tipoDTO.esTuDenominacion("Longitud")) {
            denominacion = "Longitud";
        } else if (tipoDTO.esTuDenominacion("Frecuencia")) {
            denominacion = "Frecuencia";
        }

        // Map the entity to DTO and set the denomination
        DetalleMuestraSismicaDTO detalleMuestraSisimcaDTO = detalleMuestraSismicaMapper.toDTO(detalleMuestraSismicaEntity);
        detalleMuestraSisimcaDTO.setDenominacion(denominacion);

        return detalleMuestraSisimcaDTO;
    }


    @Transactional(readOnly = true)
    public DetalleMuestraSismica obtenerPorId(Long id) {
        return detalleMuestraSismicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<DetalleMuestraSismica> obtenerTodosNoDTO() {
        List<DetalleMuestraSismica> entidades = detalleMuestraSismicaRepository.findAll();
        return entidades;
    }

    @Transactional(readOnly = true)
    public List<DetalleMuestraSismicaDTO> obtenerTodosDTO() {
        return detalleMuestraSismicaRepository.findAll()
            .stream()
            .map(entidad -> detalleMuestraSismicaMapper.toDTO(entidad))
            .toList();
    }

    @Transactional
    public DetalleMuestraSismicaDTO crearDesdeDTO(DetalleMuestraSismicaDTO dto) {
        DetalleMuestraSismica entidad = detalleMuestraSismicaMapper.toEntity(dto);
        DetalleMuestraSismica guardado = detalleMuestraSismicaRepository.save(entidad);
        return detalleMuestraSismicaMapper.toDTO(guardado);
    }

    @Transactional
    public DetalleMuestraSismicaDTO actualizarDesdeDTO(Long id, DetalleMuestraSismicaDTO dto) {
        return detalleMuestraSismicaRepository.findById(id)
            .map(existing -> {
                DetalleMuestraSismica entidadActualizada = detalleMuestraSismicaMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                DetalleMuestraSismica guardado = detalleMuestraSismicaRepository.save(entidadActualizada);
                return detalleMuestraSismicaMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}