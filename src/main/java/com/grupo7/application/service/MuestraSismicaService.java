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

    /**
     * Obtiene los detalles de muestra sísmica válidos para una MuestraSismica específica.
     * Se espera que la MuestraSismica ahora tenga una lista de DetalleMuestraSismica.
     *
     * @param muestraSismicaId El ID de la MuestraSismica de la cual se quieren obtener los detalles.
     * @return Una lista de DetalleMuestraSismica que cumplen con la condición de validez.
     */
    public List<DetalleMuestraSismica> getDatos(Long muestraSismicaId) {
        // Definir la lista de Detalles de la Muestra Sísmica Iterada
        List<DetalleMuestraSismica> detallesValidos = new ArrayList<>(); 

        // Buscar la Muestra Sísmica por su Id
        MuestraSismica muestraSismica = obtenerPorId(muestraSismicaId);

        // Verificar si la MuestraSismica existe y tiene detalles asociados
        if (muestraSismica != null && muestraSismica.getDetallesMuestra() != null) {
            // Iterar sobre los detalles de muestra sísmica directamente asociados a esta MuestraSismica
            for (DetalleMuestraSismica detalleMuestraSismicaIterada : muestraSismica.getDetallesMuestra()) {

                // Verificar que dicho detalle esté asociado a cualquiera de los tipos de datos buscados
                // Asumiendo que detalleMuestraSismicaService.getDatos(id) es un método de validación/filtro
                if (detalleMuestraSismicaService.getDatos(detalleMuestraSismicaIterada.getId())) {
                    // Entonces se lo agrega a la lista
                    detallesValidos.add(detalleMuestraSismicaIterada);
                }
            }
        }
        
        // Si la lista no está vacía, se retorna la lista con los detalles válidos 
        if (!detallesValidos.isEmpty()) {
            return detallesValidos;
        } else {
            return List.of(); // Retorna una lista inmutable vacía si no hay detalles válidos
        }
    }

    /**
     * Obtiene una MuestraSismica por su ID.
     * @param id El ID de la MuestraSismica.
     * @return La entidad MuestraSismica.
     * @throws RuntimeException si la MuestraSismica no es encontrada.
     */
    public MuestraSismica obtenerPorId(Long id) {
        return muestraSismicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Muestra no encontrada: " + id));
    }   

    /**
     * Obtiene todas las entidades MuestraSismica sin convertirlas a DTOs.
     * @return Una lista de entidades MuestraSismica.
     */
    public List<MuestraSismica> obtenerTodosNoDTO() {
        List<MuestraSismica> entidades = muestraSismicaRepository.findAll();
        return entidades;
    }

    /**
     * Obtiene todas las MuestraSismica convertidas a DTOs.
     * @return Una lista de MuestraSismicaDTO.
     */
    public List<MuestraSismicaDTO> obtenerTodosDTO() {
        return muestraSismicaRepository.findAll()
            .stream()
            .map(entidad -> muestraSismicaMapper.toDTO(entidad))
            .toList();
    }

    /**
     * Crea una nueva MuestraSismica a partir de un DTO.
     * @param dto El DTO de la MuestraSismica a crear.
     * @return El DTO de la MuestraSismica guardada.
     */
    public MuestraSismicaDTO crearDesdeDTO(MuestraSismicaDTO dto) {
        MuestraSismica entidad = muestraSismicaMapper.toEntity(dto);
        MuestraSismica guardado = muestraSismicaRepository.save(entidad);
        return muestraSismicaMapper.toDTO(guardado);
    }
    
    /**
     * Actualiza una MuestraSismica existente a partir de un DTO.
     * @param id El ID de la MuestraSismica a actualizar.
     * @param dto El DTO con los datos actualizados.
     * @return El DTO de la MuestraSismica actualizada.
     * @throws RuntimeException si la MuestraSismica no es encontrada.
     */
    public MuestraSismicaDTO actualizarDesdeDTO(Long id, MuestraSismicaDTO dto) {
        return muestraSismicaRepository.findById(id)
            .map(existing -> {
                MuestraSismica entidadActualizada = muestraSismicaMapper.toEntity(dto);
                entidadActualizada.setId(id); // Asegúrate de mantener el id
                MuestraSismica guardado = muestraSismicaRepository.save(entidadActualizada);
                return muestraSismicaMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("MuestraSismica no encontrada con id: " + id));
    }
}
