package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional; // Keep if findById is used
import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;

// Services (keep only if their specific methods are still used for business logic/validation, not fetching)
import com.grupo7.application.service.MuestraSismicaService; // For potential business logic/validation
import com.grupo7.application.service.DetalleMuestraSismicaService; // For potential business logic/validation


@Service
public class SerieTemporalService {

    private final SerieTemporalRepository serieTemporalRepository;
    private final SerieTemporalMapper serieTemporalMapper;
    private final MuestraSismicaService muestraSismicaService; // Keep if its getDatos method is for validation
    private final DetalleMuestraSismicaService detalleMuestraSismicaService; // Keep if its getDatos method is for validation
    private final MuestraSismicaMapper muestraSismicaMapper;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;


    @Autowired
    public SerieTemporalService(SerieTemporalRepository serieTemporalRepository, SerieTemporalMapper serieTemporalMapper,
                                MuestraSismicaService muestraSismicaService,
                                DetalleMuestraSismicaService detalleMuestraSismicaService,
                                MuestraSismicaMapper muestraSismicaMapper,
                                DetalleMuestraSismicaMapper detalleMuestraSismicaMapper) {
        this.serieTemporalRepository = serieTemporalRepository;
        this.serieTemporalMapper = serieTemporalMapper;
        this.muestraSismicaService = muestraSismicaService;
        this.detalleMuestraSismicaService = detalleMuestraSismicaService;
        this.muestraSismicaMapper = muestraSismicaMapper;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
    }

    /**
     * Maps a SerieTemporal entity and its associated MuestraSismica and DetalleMuestraSismica
     * to a hierarchical SerieTemporalDTO structure, mirroring the entity graph.
     * Applies validation/filtering for DetalleMuestraSismica if provided by DetalleMuestraSismicaService.
     *
     * @param serieTemporalEntity The SerieTemporal entity to process.
     * @return A SerieTemporalDTO representing the full hierarchy.
     */
    @Transactional(readOnly = true) // This method only reads data
    public SerieTemporalDTO getSerieTemporalWithFullHierarchy(SerieTemporal serieTemporalEntity) {
        if (serieTemporalEntity == null) {
            return null;
        }

        // 1. Map SerieTemporal entity to its DTO
        // Start with the base SerieTemporalDTO fields. If serieTemporalMapper can handle nested, even better.
        SerieTemporalDTO serieTemporalDTO = serieTemporalMapper.toDTO(serieTemporalEntity);

        // Ensure the nested list is initialized
        List<MuestraSismicaDTO> muestrasSismicasDTOs = new ArrayList<>();

        // 2. Iterate through MuestraSismica entities
        if (serieTemporalEntity.getMuestrasSismicas() != null) {
            for (MuestraSismica muestraSismicaEntity : serieTemporalEntity.getMuestrasSismicas()) {
                // 2.1. Map MuestraSismica entity to its DTO
                MuestraSismicaDTO muestraSismicaDTO = muestraSismicaMapper.toDTO(muestraSismicaEntity);

                // Ensure the nested list is initialized
                List<DetalleMuestraSismicaDTO> detallesMuestraDTOs = new ArrayList<>();

                // 2.2. Iterate through DetalleMuestraSismica entities
                if (muestraSismicaEntity.getDetallesMuestra() != null) {
                    for (DetalleMuestraSismica detalleMuestraSismicaEntity : muestraSismicaEntity.getDetallesMuestra()) {
                        // Apply validation/filter from DetalleMuestraSismicaService if it's a business rule
                        // Assuming detalleMuestraSismicaService.getDatos(id) is a boolean validation method
                        if (detalleMuestraSismicaService.getDatos(detalleMuestraSismicaEntity.getId())) {
                            // 2.3. Map DetalleMuestraSismica entity to its DTO
                            detallesMuestraDTOs.add(detalleMuestraSismicaMapper.toDTO(detalleMuestraSismicaEntity));
                        }
                    }
                }
                // Set the list of mapped details to the MuestraSismicaDTO
                muestraSismicaDTO.setDetallesMuestra(detallesMuestraDTOs);
                // Add the populated MuestraSismicaDTO to the list for SerieTemporalDTO
                muestrasSismicasDTOs.add(muestraSismicaDTO);
            }
        }
        // Set the list of mapped muestrasSismicas to the SerieTemporalDTO
        serieTemporalDTO.setMuestrasSismicas(muestrasSismicasDTOs);

        return serieTemporalDTO;
    }


    // The previous getDatos method for filtering entities is removed or renamed
    // to avoid confusion with the new hierarchical DTO mapping.
    // If its filtering logic is still needed for other purposes, keep it under a different name.

    // Original basic CRUD methods (kept as is)

    @Transactional(readOnly = true)
    public List<SerieTemporal> obtenerTodosNoDTO() {
        List<SerieTemporal> entidades = serieTemporalRepository.findAll();
        return entidades;
    }

    @Transactional(readOnly = true)
    public List<SerieTemporalDTO> obtenerTodosDTO() {
        // This method will now return SerieTemporalDTOs which include nested MuestraSismicaDTOs
        // and DetalleMuestraSismicaDTOs if the mappers are configured to do so,
        // or if you iterate and call getSerieTemporalWithFullHierarchy for each.
        // For a simple findAll().stream().map(mapper::toDTO), ensure your SerieTemporalMapper
        // handles the nested mapping or call the new method.
        return serieTemporalRepository.findAll()
            .stream()
            // To ensure full hierarchy, you might need to fetch entities with joins
            // and then map them using the new hierarchical method.
            // For simplicity here, assuming mapper is smart or entities are fetched
            // or you explicitly call the hierarchical method.
            .map(entity -> getSerieTemporalWithFullHierarchy(entity)) // Use the new method to get full hierarchy
            .toList();
    }

    @Transactional
    public SerieTemporalDTO crearDesdeDTO(SerieTemporalDTO dto) {
        // When creating from DTO, you'll need to handle nested DTOs to entities
        SerieTemporal entidad = serieTemporalMapper.toEntity(dto);
        // If DTO contains nested MuestraSismicaDTOs and DetalleMuestraSismicaDTOs,
        // your mappers will need to handle mapping these back to entities
        // and setting relationships correctly before saving the root entity.
        SerieTemporal guardado = serieTemporalRepository.save(entidad);
        return serieTemporalMapper.toDTO(guardado); // Or getSerieTemporalWithFullHierarchy(guardado) if full DTO is needed
    }
    
    @Transactional
    public SerieTemporalDTO actualizarDesdeDTO(Long id, SerieTemporalDTO dto) {
        return serieTemporalRepository.findById(id)
            .map(existing -> {
                // When updating, you'll need to handle nested DTOs to entities
                SerieTemporal entidadActualizada = serieTemporalMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                // Similar to create, handle nested DTOs -> entities if your mappers don't do it automatically
                SerieTemporal guardado = serieTemporalRepository.save(entidadActualizada);
                return serieTemporalMapper.toDTO(guardado); // Or getSerieTemporalWithFullHierarchy(guardado)
            })
            .orElseThrow(() -> new RuntimeException("SerieTemporal no encontrada con id: " + id));
    }
}
