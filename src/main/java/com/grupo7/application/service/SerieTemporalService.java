package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors; // Added for stream operations

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
import com.grupo7.application.dto.SerieTemporalDetalleDTO; // Import the detailed DTO

// Mappers
import com.grupo7.application.mapper.SerieTemporalMapper;
import com.grupo7.application.mapper.MuestraSismicaMapper;
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;

@Service
public class SerieTemporalService {

    private final SerieTemporalRepository serieTemporalRepository;
    private final SerieTemporalMapper serieTemporalMapper;
    // Assuming MuestraSismicaService and DetalleMuestraSismicaService are needed for their getDatos methods
    private final MuestraSismicaService muestraSismicaService;
    private final DetalleMuestraSismicaService detalleMuestraSismicaService;
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


    @Transactional(readOnly = true)
    public SerieTemporalDetalleDTO getDatos(SerieTemporal serieTemporalEntity) {

        if (serieTemporalEntity == null) {
            return null;
        }

        List<MuestraSismicaDTO> muestrasSismicasDTOsForSerie = new ArrayList<>();

        if (serieTemporalEntity.getMuestrasSismicas() != null) {
            muestrasSismicasDTOsForSerie = serieTemporalEntity.getMuestrasSismicas().stream()
                // Now calling the updated getDatos method from MuestraSismicaService
                .map(muestraSismicaEntity -> muestraSismicaService.getDatos(muestraSismicaEntity.getId()))
                .collect(Collectors.toList());
        }

        String currentCodigoEstacion = null;
        if (serieTemporalEntity.getSismografo() != null && serieTemporalEntity.getSismografo().getEstacionSismologica() != null) {
            currentCodigoEstacion = serieTemporalEntity.getSismografo().getEstacionSismologica().getCodigoEstacion();
        }

        SerieTemporalDetalleDTO serieDetalleDTO = new SerieTemporalDetalleDTO(
            serieTemporalEntity.getId(),
            serieTemporalEntity.getCondicionAlarma(),
            serieTemporalEntity.getFechaHoraRegistros(),
            currentCodigoEstacion,
            muestrasSismicasDTOsForSerie
        );

        return serieDetalleDTO;
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
            .map(entity -> serieTemporalMapper.toDTO(entity)) // Assuming serieTemporalMapper.toDTO can handle basic mapping
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