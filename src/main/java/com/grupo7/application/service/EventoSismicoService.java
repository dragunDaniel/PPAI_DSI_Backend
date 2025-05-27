package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.Set; // Needed for Set collections if used in entities like getMuestrasSismicas()

// Entities
import com.grupo7.application.entity.EventoSismico;
import com.grupo7.application.entity.SerieTemporal;
import com.grupo7.application.entity.MuestraSismica; // IMPORTANT: Added MuestraSismica entity import
import com.grupo7.application.entity.DetalleMuestraSismica;
import com.grupo7.application.entity.Estado;

// Repositories
import com.grupo7.application.repository.EventoSismicoRepository;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.CambioEstadoDTO;
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.SerieTemporalDetalleDTO;
import com.grupo7.application.dto.SerieTemporalDTO; // This DTO is for a single SerieTemporal, not the aggregated data
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;
import com.grupo7.application.dto.SismografoDTO;
import com.grupo7.application.dto.DatosRegistradosDTO; // IMPORTANT: Correct DTO for the aggregated return
import com.grupo7.application.dto.MuestraSismicaDTO; // IMPORTANT: Added MuestraSismicaDTO import

// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;
import com.grupo7.application.mapper.EstadoMapper;
import com.grupo7.application.mapper.SerieTemporalMapper;
import com.grupo7.application.mapper.MuestraSismicaMapper; // IMPORTANT: Added MuestraSismicaMapper
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;
import com.grupo7.application.mapper.SismografoMapper;

// Services
import com.grupo7.application.service.CambioEstadoService;
import com.grupo7.application.service.EstadoService;
import com.grupo7.application.service.SerieTemporalService;

@Service
public class EventoSismicoService {

    private final EventoSismicoRepository eventoSismicoRepository;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final CambioEstadoService cambioEstadoService;
    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;
    private final SerieTemporalService serieTemporalService;
    private final SerieTemporalMapper serieTemporalMapper;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;
    private final SismografoMapper sismografoMapper;
    private final MuestraSismicaMapper muestraSismicaMapper; // Inyectar MuestraSismicaMapper

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper, 
                                CambioEstadoService cambioEstadoService, EstadoService estadoService, EstadoMapper estadoMapper,
                                SerieTemporalService serieTemporalService, SerieTemporalMapper serieTemporalMapper,
                                DetalleMuestraSismicaMapper detalleMuestraSismicaMapper, SismografoMapper sismografoMapper,
                                MuestraSismicaMapper muestraSismicaMapper) { // Add MuestraSismicaMapper to constructor
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.cambioEstadoService = cambioEstadoService;
        this.estadoService = estadoService;
        this.estadoMapper = estadoMapper;
        this.serieTemporalService = serieTemporalService;
        this.serieTemporalMapper = serieTemporalMapper;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
        this.sismografoMapper = sismografoMapper;
        this.muestraSismicaMapper = muestraSismicaMapper; // Initialize MuestraSismicaMapper
    }

    public List<EventoSismicoDTO> esAutoDetectadoOPendienteDeRevision() {
        
        // Lista de Eventos Sismicos Filtrados
        List<EventoSismicoDTO> eventosSismicosFiltradosDTO = new ArrayList<>();

        // Mientras haya eventos sismicos
        for (EventoSismico eventoSismico : obtenerTodosNoDTO()) {
            
            // busco el estado actual para el evento sismico iterado
            CambioEstadoDTO cambioEstadoActual = cambioEstadoService.obtenerCambioEstadoActual(eventoSismico);

            // Evitando llamar a cambios de estado con id nulo
            if (cambioEstadoActual == null || cambioEstadoActual.getEstado() == null) {
                continue;
            }

            // Verificando si el estado actual es AutoDetectado o PendienteDeRevision
            if ((cambioEstadoService.sosAutoDetectado(cambioEstadoActual.getEstado().getId())) ||
            (cambioEstadoService.sosPendienteDeRevision(cambioEstadoActual.getEstado().getId()))) {
                
                // Agregando el evento sismico a la lista de eventos sismicos filtrados
                eventosSismicosFiltradosDTO.add(eventoSismicoMapper.toDTO(eventoSismico));

            } 
        }

        // Retornando los datos principales (DTO) de los eventos sismicos filtrados
        return eventosSismicosFiltradosDTO;
    } 

    /**
     * Obtiene la entidad EventoSismico a partir de su DTO, asegurando que las relaciones necesarias
     * para DatosRegistradosDTO estén cargadas (JOIN FETCH).
     * @param dto El DTO del EventoSismico.
     * @return La entidad EventoSismico con detalles cargados.
     * @throws RuntimeException si el EventoSismico no es encontrado.
     */
    @Transactional(readOnly = true) // Este método solo lee datos
    private EventoSismico obtenerEntidadDesdeDTO(EventoSismicoDTO dto) {
        // Ensure you have this method in your EventoSismicoRepository:
        // @Query("SELECT e FROM EventoSismico e " +
        //       "LEFT JOIN FETCH e.alcanceSismo " +
        //       "LEFT JOIN FETCH e.clasificacionSismo " +
        //       "LEFT JOIN FETCH e.origenGeneracion " +
        //       "LEFT JOIN FETCH e.seriesTemporales st " +
        //       "LEFT JOIN FETCH st.sismografo s " +
        //       "LEFT JOIN FETCH st.muestrasSismicas ms " + // Ensure this is correct in your entity
        //       "LEFT JOIN FETCH ms.detallesMuestra dm " + // Ensure this is correct in your entity
        //       "WHERE e.id = :id")
        // Optional<EventoSismico> findByIdWithDetails(@Param("id") Long id);
        return eventoSismicoRepository.findByIdWithDetails(dto.getId())
            .orElseThrow(() -> new RuntimeException("EventoSismico no encontrado con id: " + dto.getId()));
    }

    public void bloquearPorRevision(EventoSismicoDTO eventoSismicoSeleccionadoDTO, LocalDateTime fechaHoraActual, EstadoDTO estadoBloqueadoDTO) {

        // Buscar el cambio de estado actual para el evento seleccionado
        for (CambioEstadoDTO cambioEstadoDTO : cambioEstadoService.obtenerTodosDTO()) {
    
            // Si el cambio de estado pertenece al evento seleccionado y es el actual
            if (cambioEstadoDTO.getEventoSismico() != null &&
                cambioEstadoDTO.getEventoSismico().getId().equals(eventoSismicoSeleccionadoDTO.getId()) &&
                cambioEstadoDTO.esEstadoActual()) {
    
                // Finalizar el estado actual colocando fecha de fin
                cambioEstadoDTO.setFechaHoraFin(fechaHoraActual);
    
                // Actualizar el cambio de estado en la base
                CambioEstadoDTO cambioEstadoGuardado = cambioEstadoService.actualizarDesdeDTO(cambioEstadoDTO.getId(), cambioEstadoDTO);
                System.out.println("El cambio de estado guardado es: " + cambioEstadoGuardado);
    
                // Crear un nuevo cambio de estado con estado "bloqueado"
                CambioEstadoDTO cambioEstadoBloqueado = new CambioEstadoDTO();
                cambioEstadoBloqueado.setFechaHoraInicio(fechaHoraActual);
                // **FIX:** Directly set the EventoSismicoDTO as it's a DTO-to-DTO relationship
                cambioEstadoBloqueado.setEventoSismico(eventoSismicoSeleccionadoDTO);
    
                // Buscar el Estado persistido desde el repositorio (o servicio) antes de setearlo al nuevo cambioEstado que apunta a Estado Bloqueado
                Long estadoId = estadoBloqueadoDTO.getId();
                System.out.println(" -----__---------> EL ID DEL ESATDO ES: ---> " + estadoId);
    
                // Evitando llamar a cambios de estado con id nulo
                if (estadoId == null) {
                    throw new RuntimeException("El DTO de Estado no contiene un ID válido.");
                }
    
                // Primero asignar el estado
                // **FIX:** Directly set the EstadoDTO, as your CambioEstadoDTO expects an EstadoDTO
                cambioEstadoBloqueado.setEstado(estadoBloqueadoDTO);
    
                // Después imprimir con seguridad
                System.out.println("Estado persistido asignado al cambio de estado: " + cambioEstadoBloqueado.getEstado().getId());
    
                // Guardar el nuevo cambio de estado
                cambioEstadoService.crearDesdeDTO(cambioEstadoBloqueado);
    
                // Terminado el ciclo
                break;
            }
        }
    }

    /**
     * Busca los datos registrados para un evento sísmico seleccionado, mapeando todas las entidades
     * relacionadas a sus respectivos DTOs para evitar problemas de serialización.
     * Ahora construye un DTO jerárquico.
     * @param eventoSismicoSeleccionadoDTO El DTO del evento sísmico seleccionado.
     * @return Un DatosRegistradosDTO con la información detallada y jerárquica.
     */
    @Transactional(readOnly = true) // This method only reads data
    public DatosRegistradosDTO buscarDatosRegistrados(EventoSismicoDTO eventoSismicoSeleccionadoDTO) {
        // 1. Obtener el evento sismico entidad con todas sus relaciones cargadas (JOIN FETCH)
        EventoSismico eventoSismicoEntidad = obtenerEntidadDesdeDTO(eventoSismicoSeleccionadoDTO);

        // 2. Extraer los nombres directamente de las entidades cargadas
        String alcanceSismoNombre = (eventoSismicoEntidad.getAlcanceSismo() != null) ? eventoSismicoEntidad.getAlcanceSismo().getNombre() : null;
        String clasificacionSismoNombre = (eventoSismicoEntidad.getClasificacionSismo() != null) ? eventoSismicoEntidad.getClasificacionSismo().getNombre() : null;
        String origenGeneracionNombre = (eventoSismicoEntidad.getOrigenGeneracion() != null) ? eventoSismicoEntidad.getOrigenGeneracion().getNombre() : null;

        // 3. Procesar las series temporales y construir la lista de SerieTemporalDetalleDTO
        List<SerieTemporalDetalleDTO> seriesTemporalesDetallesDTOs = new ArrayList<>();

        if (eventoSismicoEntidad.getSeriesTemporales() != null) {
            for (SerieTemporal serieTemporal : eventoSismicoEntidad.getSeriesTemporales()) {
                // List to hold MuestraSismicaDTOs for the current SerieTemporal
                List<MuestraSismicaDTO> muestrasSismicasDTOsForSerie = new ArrayList<>();

                // Iterate through MuestraSismica entities for the current SerieTemporal
                if (serieTemporal.getMuestrasSismicas() != null) {
                    for (MuestraSismica muestraSismica : serieTemporal.getMuestrasSismicas()) {
                        // List to hold DetalleMuestraSismicaDTOs for the current MuestraSismica
                        List<DetalleMuestraSismicaDTO> detallesMuestraDTOsForMuestra = new ArrayList<>();

                        // Iterate through DetalleMuestraSismica entities for the current MuestraSismica
                        if (muestraSismica.getDetallesMuestra() != null) {
                            for (DetalleMuestraSismica detalleMuestraSismica : muestraSismica.getDetallesMuestra()) {
                                // Apply validation/filter from DetalleMuestraSismicaService if it's a business rule
                                // Assuming detalleMuestraSismicaService.getDatos(id) is a boolean validation method
                                // If you removed DetalleMuestraSismicaService, you might need to adjust this logic.
                                // For now, directly map to DTO.
                                // If you need validation, ensure DetalleMuestraSismicaService is injected and its method exists.
                                // if (detalleMuestraSismicaService.getDatos(detalleMuestraSismica.getId())) {
                                    detallesMuestraDTOsForMuestra.add(detalleMuestraSismicaMapper.toDTO(detalleMuestraSismica));
                                // }
                            }
                        }
                        // Create MuestraSismicaDTO using its constructor
                        MuestraSismicaDTO muestraSismicaDTO = new MuestraSismicaDTO(
                            muestraSismica.getId(),
                            muestraSismica.getFechaHoraMuestra(),
                            detallesMuestraDTOsForMuestra // Pass the list of mapped details
                        );
                        muestrasSismicasDTOsForSerie.add(muestraSismicaDTO);
                    }
                }

                // Create SerieTemporalDetalleDTO using its CORRECT constructor
                SerieTemporalDetalleDTO serieDetalleDTO = new SerieTemporalDetalleDTO(
                    serieTemporal.getId(),
                    serieTemporal.getCondicionAlarma(),
                    serieTemporal.getFechaHoraRegistros(),
                    (serieTemporal.getSismografo() != null) ? serieTemporal.getSismografo().getIdentificador() : null,
                    muestrasSismicasDTOsForSerie // Pass the list of mapped MuestraSismicaDTOs
                );
                seriesTemporalesDetallesDTOs.add(serieDetalleDTO);
            }
        }

        // IMPORTANT: Return DatosRegistradosDTO, which now holds the list of SerieTemporalDetalleDTOs
        return new DatosRegistradosDTO(alcanceSismoNombre, clasificacionSismoNombre, origenGeneracionNombre, seriesTemporalesDetallesDTOs);
    }

    // Métodos CRUD básicos:

    /**
     * Obtiene todas las entidades EventoSismico sin convertirlas a DTOs.
     * @return Una lista de entidades EventoSismico.
     */
    @Transactional(readOnly = true)
    public List<EventoSismico> obtenerTodosNoDTO() {
        List<EventoSismico> entidades = eventoSismicoRepository.findAll();
        return entidades;
    }

    /**
     * Obtiene todas las EventoSismico convertidas a DTOs.
     * @return Una lista de EventoSismicoDTO.
     */
    @Transactional(readOnly = true)
    public List<EventoSismicoDTO> obtenerTodosDTO() {
        return eventoSismicoRepository.findAll()
            .stream()
            .map(eventoSismicoMapper::toDTO)
            .toList();
    }

    /**
     * Crea una nueva EventoSismico a partir de un DTO.
     * @param dto El DTO del EventoSismico a crear.
     * @return El DTO del EventoSismico guardado.
     */
    @Transactional
    public EventoSismicoDTO crearDesdeDTO(EventoSismicoDTO dto) {
        EventoSismico entidad = eventoSismicoMapper.toEntity(dto);
        EventoSismico guardado = eventoSismicoRepository.save(entidad);
        return eventoSismicoMapper.toDTO(guardado);
    }
    
    /**
     * Actualiza una EventoSismico existente a partir de un DTO.
     * @param id El ID del EventoSismico a actualizar.
     * @param dto El DTO con los datos actualizados.
     * @return El DTO del EventoSismico actualizada.
     * @throws RuntimeException si la EventoSismico no es encontrada.
     */
    @Transactional
    public EventoSismicoDTO actualizarDesdeDTO(Long id, EventoSismicoDTO dto) {
        return eventoSismicoRepository.findById(id)
            .map(existing -> {
                EventoSismico entidadActualizada = eventoSismicoMapper.toEntity(dto);
                entidadActualizada.setId(id); // asegúrate de mantener el id
                EventoSismico guardado = eventoSismicoRepository.save(entidadActualizada);
                return eventoSismicoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("eventoSismico no encontrado con id: " + id));
    }
}
