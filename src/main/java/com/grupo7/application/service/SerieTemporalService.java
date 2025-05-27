package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;

// Entities
import com.grupo7.application.entity.SerieTemporal;
import com.grupo7.application.entity.MuestraSismica;
import com.grupo7.application.entity.DetalleMuestraSismica;

// Repositories
import com.grupo7.application.repository.SerieTemporalRepository;

// DTOs
import com.grupo7.application.dto.SerieTemporalDTO;
import com.grupo7.application.dto.MuestraSismicaDTO;
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;

// Mappers
import com.grupo7.application.mapper.SerieTemporalMapper;
import com.grupo7.application.mapper.MuestraSismicaMapper;

// Services 
import com.grupo7.application.service.MuestraSismicaService;

@Service
public class SerieTemporalService {

    private final SerieTemporalRepository serieTemporalRepository;
    private final SerieTemporalMapper serieTemporalMapper;
    private final MuestraSismicaService muestraSismicaService; 

    @Autowired
    public SerieTemporalService(SerieTemporalRepository serieTemporalRepository, SerieTemporalMapper serieTemporalMapper, MuestraSismicaService muestraSismicaService) {

        this.serieTemporalRepository = serieTemporalRepository;
        this.serieTemporalMapper = serieTemporalMapper;
        this.muestraSismicaService = muestraSismicaService; 
    }

    // Obtener Datos (Muetra Sismicas para una Serie Temporal)
    public List<DetalleMuestraSismica> getDatos(SerieTemporal serieTemporalIterada) {
        
        // Definiendo la lista de datos buscados
        List<DetalleMuestraSismica> datosValidos = new ArrayList<>();
        
        // Obtener los datos validos para de las muestras sismicas para la serie temporal iterada
        datosValidos = muestraSismicaService.getDatos(serieTemporalIterada.getMuestraSismica().getId());
        
        return datosValidos;

    }

    public List<SerieTemporal> obtenerTodosNoDTO() {
        List<SerieTemporal> entidades = serieTemporalRepository.findAll();
        return entidades;
    }

    public List<SerieTemporalDTO> obtenerTodosDTO() {
        return serieTemporalRepository.findAll()
            .stream()
            .map(entidad -> serieTemporalMapper.toDTO(entidad))
            .toList();
    }

    public SerieTemporalDTO crearDesdeDTO(SerieTemporalDTO dto) {
        SerieTemporal entidad = serieTemporalMapper.toEntity(dto);
        SerieTemporal guardado = serieTemporalRepository.save(entidad);
        return serieTemporalMapper.toDTO(guardado);
    }
    
    public SerieTemporalDTO actualizarDesdeDTO(Long id, SerieTemporalDTO dto) {
        return serieTemporalRepository.findById(id)
            .map(existing -> {
                SerieTemporal entidadActualizada = serieTemporalMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                SerieTemporal guardado = serieTemporalRepository.save(entidadActualizada);
                return serieTemporalMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}    