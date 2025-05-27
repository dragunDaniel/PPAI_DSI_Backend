package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;

// Entities
import com.grupo7.application.entity.MuestraSismica;
import com.grupo7.application.entity.DetalleMuestraSismica; 

// Repositories
import com.grupo7.application.repository.MuestraSismicaRepository;

// DTOs
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;
import com.grupo7.application.dto.MuestraSismicaDTO;

// Mappers
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;
import com.grupo7.application.mapper.MuestraSismicaMapper;

// Services 
import com.grupo7.application.service.DetalleMuestraSismicaService;

@Service
public class MuestraSismicaService {

    private final MuestraSismicaRepository muestraSismicaRepository;
    private final MuestraSismicaMapper muestraSismicaMapper;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;
    private final DetalleMuestraSismicaService detalleMuestraSismicaService;

    @Autowired
    public MuestraSismicaService(MuestraSismicaRepository muestraSismicaRepository, MuestraSismicaMapper muestraSismicaMapper,
     DetalleMuestraSismicaMapper detalleMuestraSismicaMapper, DetalleMuestraSismicaService detalleMuestraSismicaService) {

        this.muestraSismicaRepository = muestraSismicaRepository;
        this.muestraSismicaMapper = muestraSismicaMapper;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
        this.detalleMuestraSismicaService = detalleMuestraSismicaService;
    }

    // Obtener Datos (Muetra Sismicas para una Serie Temporal)
    public List<DetalleMuestraSismica> getDatos(Long muestraSismicaIteradaId) {
        
        // Definiendo la lista de Detalles de la Muestra Sismica Iterada
        List<DetalleMuestraSismica> detallesValidos = new ArrayList<>(); 

        // Buscar la Muestra Sismica Itereada por su Id
        MuestraSismica muestraSismicaIterada = obtenerPorId(muestraSismicaIteradaId);

        // Iterando sobre los detalles de muestra sismica iterada
        for (DetalleMuestraSismica detalleMuestraSismicaIterada : detalleMuestraSismicaService.obtenerTodosNoDTO()) {

            // Verificando si el detalle conrresponde con a la muestraSismicaIterada
            if (detalleMuestraSismicaIterada.getId() == muestraSismicaIterada.getDetalleMuestraSismica().getId()) {

                // Verificando que dicho detalle este asociado a cualquiera de los tipos de datos buscados
                if (detalleMuestraSismicaService.getDatos(detalleMuestraSismicaIterada.getId())) {

                    // Entonces se lo agrega a la lista
                    detallesValidos.add(detalleMuestraSismicaIterada);
                }

            }

        }
        
        // Si la lista no esta vacia, se retorna la lista con los detalles validos 
        if (!detallesValidos.isEmpty()) {
            return detallesValidos;
        } else {
            return List.of();
        }
        
    }

    public MuestraSismica obtenerPorId(Long id) {
        return muestraSismicaRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Muestra no encontrada: " + id));
    }  

    public List<MuestraSismica> obtenerTodosNoDTO() {
        List<MuestraSismica> entidades = muestraSismicaRepository.findAll();
        return entidades;
    }

    public List<MuestraSismicaDTO> obtenerTodosDTO() {
        return muestraSismicaRepository.findAll()
            .stream()
            .map(entidad -> muestraSismicaMapper.toDTO(entidad))
            .toList();
    }

    public MuestraSismicaDTO crearDesdeDTO(MuestraSismicaDTO dto) {
        MuestraSismica entidad = muestraSismicaMapper.toEntity(dto);
        MuestraSismica guardado = muestraSismicaRepository.save(entidad);
        return muestraSismicaMapper.toDTO(guardado);
    }
    
    public MuestraSismicaDTO actualizarDesdeDTO(Long id, MuestraSismicaDTO dto) {
        return muestraSismicaRepository.findById(id)
            .map(existing -> {
                MuestraSismica entidadActualizada = muestraSismicaMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                MuestraSismica guardado = muestraSismicaRepository.save(entidadActualizada);
                return muestraSismicaMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}    