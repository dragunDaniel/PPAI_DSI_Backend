package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for @Transactional
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    
    @Transactional(readOnly = true)
    public MuestraSismicaDTO getDatos(Long muestraSismicaId) {
        // Buscar la Muestra Sísmica por su Id
        MuestraSismica muestraSismicaEntity = obtenerPorId(muestraSismicaId);

        if (muestraSismicaEntity == null) {
            return null;
        }

        List<DetalleMuestraSismicaDTO> detallesMuestraDTOsForMuestra = new ArrayList<>();

        if (muestraSismicaEntity.getDetallesMuestra() != null) {
            detallesMuestraDTOsForMuestra = muestraSismicaEntity.getDetallesMuestra().stream()
                // Call the new overloaded getDatos method in DetalleMuestraSismicaService
                .map(detalleMuestraSismicaService::getDatos)
                // Filter out details if their denomination is null (as per original logic)
                .filter(dto -> dto != null && dto.getDenominacion() != null)
                .collect(Collectors.toList());
        }

        // Create MuestraSismicaDTO with its details
        return new MuestraSismicaDTO(
            muestraSismicaEntity.getId(),
            muestraSismicaEntity.getFechaHoraMuestra(),
            detallesMuestraDTOsForMuestra
        );
    }

    /**
     * Obtiene una MuestraSismica por su ID.
     * @param id El ID de la MuestraSismica.
     * @return La entidad MuestraSismica.
     * @throws RuntimeException si la MuestraSismica no es encontrada.
     */
    @Transactional(readOnly = true)
    public MuestraSismica obtenerPorId(Long id) {
        return muestraSismicaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Muestra no encontrada: " + id));
    }

    /**
     * Obtiene todas las entidades MuestraSismica sin convertirlas a DTOs.
     * @return Una lista de entidades MuestraSismica.
     */
    @Transactional(readOnly = true)
    public List<MuestraSismica> obtenerTodosNoDTO() {
        List<MuestraSismica> entidades = muestraSismicaRepository.findAll();
        return entidades;
    }

    /**
     * Obtiene todas las MuestraSismica convertidas a DTOs.
     * @return Una lista de MuestraSismicaDTO.
     */
    @Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional
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