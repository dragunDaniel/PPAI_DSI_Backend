package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;

// Entities
import com.grupo7.application.entity.DetalleMuestraSismica; 
import com.grupo7.application.entity.TipoDeDato;

// Repositories
import com.grupo7.application.repository.DetalleMuestraSismicaRepository;

// DTOs
import com.grupo7.application.dto.TipoDeDatoDTO;
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;

// Mappers
import com.grupo7.application.mapper.TipoDeDatoMapper;
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;

// Services 

@Service
public class DetalleMuestraSismicaService {

    private final DetalleMuestraSismicaRepository detalleMuestraSismicaRepository;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;
    private final TipoDeDatoMapper tipoDeDatoMapper;

    @Autowired
    public DetalleMuestraSismicaService(DetalleMuestraSismicaRepository detalleMuestraSismicaRepository, DetalleMuestraSismicaMapper detalleMuestraSismicaMapper,
        TipoDeDatoService tipoDeDatoService, TipoDeDatoMapper tipoDeDatoMapper) {
        this.detalleMuestraSismicaRepository = detalleMuestraSismicaRepository;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
        this.tipoDeDatoMapper = tipoDeDatoMapper;
    }

    // Obtener Datos (Muetra Sismicas para una Serie Temporal)
    public String getDatos(Long detalleMuestraSismicaIteradaId) {
        
        // Obteniendo el Detalle de Muestra Sismica a partir del Id
        DetalleMuestraSismica detalleMuestraSismicaIterada = obtenerPorId(detalleMuestraSismicaIteradaId);

        // Obteniendo el Tipo de Dato asociado al detalle de la muestra sismica
        TipoDeDatoDTO tipoDTO = tipoDeDatoMapper.toDTO(detalleMuestraSismicaIterada.getTipoDeDato());

        // Verificando que el tipo de dato asociado al detalle de muestra sismica sea el buscado
        if (tipoDTO.esTuDenominacion("Velocidad")) {
            return "Velocidad";
        }
        if (tipoDTO.esTuDenominacion("Longitud")) {
            return "Longitud";
        }
        if (tipoDTO.esTuDenominacion("Frecuencia")) {
            return "Frecuencia";
        }
        
        // Si no es de los datos buscados retornar null
        return null;
    }


    public DetalleMuestraSismica obtenerPorId(Long id) {
        return detalleMuestraSismicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Detalle no encontrado: " + id));
    }


    public List<DetalleMuestraSismica> obtenerTodosNoDTO() {
        List<DetalleMuestraSismica> entidades = detalleMuestraSismicaRepository.findAll();
        return entidades;
    }

    public List<DetalleMuestraSismicaDTO> obtenerTodosDTO() {
        return detalleMuestraSismicaRepository.findAll()
            .stream()
            .map(entidad -> detalleMuestraSismicaMapper.toDTO(entidad))
            .toList();
    }

    public DetalleMuestraSismicaDTO crearDesdeDTO(DetalleMuestraSismicaDTO dto) {
        DetalleMuestraSismica entidad = detalleMuestraSismicaMapper.toEntity(dto);
        DetalleMuestraSismica guardado = detalleMuestraSismicaRepository.save(entidad);
        return detalleMuestraSismicaMapper.toDTO(guardado);
    }
    
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