package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

// Entities
import com.grupo7.application.entity.EventoSismico;
import com.grupo7.application.entity.SerieTemporal;
import com.grupo7.application.entity.MuestraSismica;
import com.grupo7.application.entity.DetalleMuestraSismica;

// Repositories
import com.grupo7.application.repository.EventoSismicoRepository;

// DTOs
import com.grupo7.application.dto.EventoSismicoDTO;
import com.grupo7.application.dto.EventoSismicoBuscadoDTO;
import com.grupo7.application.dto.CambioEstadoDTO;
import com.grupo7.application.dto.EstadoDTO;
import com.grupo7.application.dto.SerieTemporalDetalleDTO;
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;
import com.grupo7.application.dto.DatosRegistradosDTO;
import com.grupo7.application.dto.MuestraSismicaDTO;
import com.grupo7.application.dto.EmpleadoDTO;

// Mappers
import com.grupo7.application.mapper.EventoSismicoMapper;
import com.grupo7.application.mapper.EventoSismicoBuscadoMapper;
import com.grupo7.application.mapper.EstadoMapper;
import com.grupo7.application.mapper.SerieTemporalMapper;
import com.grupo7.application.mapper.MuestraSismicaMapper;
import com.grupo7.application.mapper.DetalleMuestraSismicaMapper;
import com.grupo7.application.mapper.SismografoMapper;

@Service
public class EventoSismicoService {

    private final EventoSismicoRepository eventoSismicoRepository;
    private final EventoSismicoMapper eventoSismicoMapper;
    private final EventoSismicoBuscadoMapper eventoSismicoBuscadoMapper;
    private final CambioEstadoService cambioEstadoService;
    private final EstadoService estadoService;
    private final EstadoMapper estadoMapper;
    private final SerieTemporalService serieTemporalService;
    private final SerieTemporalMapper serieTemporalMapper;
    private final DetalleMuestraSismicaMapper detalleMuestraSismicaMapper;
    private final SismografoMapper sismografoMapper;
    private final MuestraSismicaMapper muestraSismicaMapper;

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper,
                                CambioEstadoService cambioEstadoService, EstadoService estadoService, EstadoMapper estadoMapper,
                                SerieTemporalService serieTemporalService, SerieTemporalMapper serieTemporalMapper,
                                DetalleMuestraSismicaMapper detalleMuestraSismicaMapper, SismografoMapper sismografoMapper,
                                MuestraSismicaMapper muestraSismicaMapper, EventoSismicoBuscadoMapper eventoSismicoBuscadoMapper) {
        this.eventoSismicoRepository = eventoSismicoRepository;
        this.eventoSismicoMapper = eventoSismicoMapper;
        this.eventoSismicoBuscadoMapper = eventoSismicoBuscadoMapper;
        this.cambioEstadoService = cambioEstadoService;
        this.estadoService = estadoService;
        this.estadoMapper = estadoMapper;
        this.serieTemporalService = serieTemporalService;
        this.serieTemporalMapper = serieTemporalMapper;
        this.detalleMuestraSismicaMapper = detalleMuestraSismicaMapper;
        this.sismografoMapper = sismografoMapper;
        this.muestraSismicaMapper = muestraSismicaMapper;
    }

    public List<EventoSismicoBuscadoDTO> esAutoDetectadoOPendienteDeRevision() {
        
        // Lista de Eventos Sismicos Filtrados
        List<EventoSismicoBuscadoDTO> eventosSismicosFiltradosDTO = new ArrayList<>();

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
                eventosSismicosFiltradosDTO.add(eventoSismicoBuscadoMapper.toBuscadoDTO(eventoSismico));


            } 
        }

        // Retornando los datos principales (DTO) de los eventos sismicos filtrados
        return eventosSismicosFiltradosDTO;
    } 

    // Obtener datos principales de los eventos sismicos
    public List<EventoSismicoDTO> obtenerDatosPrincipales(List<EventoSismicoBuscadoDTO> buscados) {
        return buscados.stream()
                       .map(eventoSismicoBuscadoMapper::toBasicDTO)  // convierte cada BuscadoDTO a simple DTO
                       .collect(Collectors.toList());
    }
    
    
    public void bloquearPorRevision(EventoSismicoDTO eventoSismicoSeleccionadoDTO, LocalDateTime fechaHoraActual, EstadoDTO estadoBloqueadoDTO) {

        for (CambioEstadoDTO cambioEstadoDTO : cambioEstadoService.obtenerTodosDTO()) {

            if (cambioEstadoDTO.getEventoSismico() != null &&
                cambioEstadoDTO.getEventoSismico().getId().equals(eventoSismicoSeleccionadoDTO.getId()) &&
                cambioEstadoDTO.esEstadoActual()) {

                cambioEstadoDTO.setFechaHoraFin(fechaHoraActual);

                CambioEstadoDTO cambioEstadoGuardado = cambioEstadoService.actualizarDesdeDTO(cambioEstadoDTO.getId(), cambioEstadoDTO);
                System.out.println("El cambio de estado guardado es: " + cambioEstadoGuardado);

                CambioEstadoDTO cambioEstadoBloqueado = new CambioEstadoDTO();
                cambioEstadoBloqueado.setFechaHoraInicio(fechaHoraActual);
                cambioEstadoBloqueado.setEventoSismico(eventoSismicoSeleccionadoDTO);

                Long estadoId = estadoBloqueadoDTO.getId();
                System.out.println(" -----__---------> EL ID DEL ESATDO ES: ---> " + estadoId);

                if (estadoId == null) {
                    throw new RuntimeException("El DTO de Estado no contiene un ID válido.");
                }

                cambioEstadoBloqueado.setEstado(estadoBloqueadoDTO);

                System.out.println("Estado persistido asignado al cambio de estado: " + cambioEstadoBloqueado.getEstado().getId());

                cambioEstadoService.crearDesdeDTO(cambioEstadoBloqueado);

                break;
            }
        }
    }
    
    @Transactional(readOnly = true)
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
                List<MuestraSismicaDTO> muestrasSismicasDTOsForSerie = new ArrayList<>();

                if (serieTemporal.getMuestrasSismicas() != null) {
                    for (MuestraSismica muestraSismica : serieTemporal.getMuestrasSismicas()) {
                        List<DetalleMuestraSismicaDTO> detallesMuestraDTOsForMuestra = new ArrayList<>();

                        if (muestraSismica.getDetallesMuestra() != null) {
                            for (DetalleMuestraSismica detalleMuestraSismica : muestraSismica.getDetallesMuestra()) {
                                detallesMuestraDTOsForMuestra.add(detalleMuestraSismicaMapper.toDTO(detalleMuestraSismica));
                            }
                        }
                        MuestraSismicaDTO muestraSismicaDTO = new MuestraSismicaDTO(
                            muestraSismica.getId(),
                            muestraSismica.getFechaHoraMuestra(),
                            detallesMuestraDTOsForMuestra
                        );
                        muestrasSismicasDTOsForSerie.add(muestraSismicaDTO);
                    }
                }

                String currentCodigoEstacion = null;
                if (serieTemporal.getSismografo() != null && serieTemporal.getSismografo().getEstacionSismologica() != null) {
                    currentCodigoEstacion = serieTemporal.getSismografo().getEstacionSismologica().getCodigoEstacion();
                }

                SerieTemporalDetalleDTO serieDetalleDTO = new SerieTemporalDetalleDTO(
                    serieTemporal.getId(),
                    serieTemporal.getCondicionAlarma(),
                    serieTemporal.getFechaHoraRegistros(),
                    (serieTemporal.getSismografo() != null) ? serieTemporal.getSismografo().getIdentificador() : null,
                    currentCodigoEstacion,
                    muestrasSismicasDTOsForSerie
                );
                seriesTemporalesDetallesDTOs.add(serieDetalleDTO);
            }
        }

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

    public void rechazar(EventoSismicoDTO eventoSismicoSeleccionadoDTO, LocalDateTime fechaHoraActual, EstadoDTO estadoRechazadoDTO, EmpleadoDTO responsableDeInspeccionDTO) {

        for (CambioEstadoDTO cambioEstadoDTO : cambioEstadoService.obtenerTodosDTO()) {

            if (cambioEstadoDTO.getEventoSismico() != null &&
                cambioEstadoDTO.getEventoSismico().getId().equals(eventoSismicoSeleccionadoDTO.getId()) &&
                cambioEstadoDTO.esEstadoActual()) {

                // Seteando el null al cambio de estado actual del evento sismico seleccionado
                cambioEstadoDTO.setFechaHoraFin(fechaHoraActual);

                // Persistiendo los cambios
                CambioEstadoDTO cambioEstadoGuardado = cambioEstadoService.actualizarDesdeDTO(cambioEstadoDTO.getId(), cambioEstadoDTO);
                System.out.println("El cambio de estado guardado es: " + cambioEstadoGuardado);

                // Creando un nuevo objeto cambio de estado que apunta al estado Rechazado
                CambioEstadoDTO cambioEstadoRechazado = new CambioEstadoDTO();
                cambioEstadoRechazado.setFechaHoraInicio(fechaHoraActual);
                cambioEstadoRechazado.setEventoSismico(eventoSismicoSeleccionadoDTO);

                // Asignando al responsable de inspeccion
                cambioEstadoRechazado.setResponsable(responsableDeInspeccionDTO);

                Long estadoId = estadoRechazadoDTO.getId();
                System.out.println(" -----__---------> EL ID DEL ESATDO ES: ---> " + estadoId);

                // Verificando que el estado existe y tiene un id asignado 
                if (estadoId == null) {
                    throw new RuntimeException("El DTO de Estado no contiene un ID válido.");
                }

                // Asignando el puntero del estado Rechazado al nuevo cambio de estado creado
                cambioEstadoRechazado.setEstado(estadoRechazadoDTO);

                System.out.println("Estado persistido asignado al cambio de estado: " + cambioEstadoRechazado.getEstado().getId());

                // Creando el CambioDeEstado a partir del DTO
                cambioEstadoService.crearDesdeDTO(cambioEstadoRechazado);

                break;
            }
        }
    }

    public void confirmar(EventoSismicoDTO eventoSismicoSeleccionadoDTO, LocalDateTime fechaHoraActual, EstadoDTO estadoConfirmadoDTO, EmpleadoDTO responsableDeInspeccionDTO) {

        for (CambioEstadoDTO cambioEstadoDTO : cambioEstadoService.obtenerTodosDTO()) {

            if (cambioEstadoDTO.getEventoSismico() != null &&
                cambioEstadoDTO.getEventoSismico().getId().equals(eventoSismicoSeleccionadoDTO.getId()) &&
                cambioEstadoDTO.esEstadoActual()) {

                // Seteando el null al cambio de estado actual del evento sismico seleccionado
                cambioEstadoDTO.setFechaHoraFin(fechaHoraActual);

                // Persistiendo los cambios
                CambioEstadoDTO cambioEstadoGuardado = cambioEstadoService.actualizarDesdeDTO(cambioEstadoDTO.getId(), cambioEstadoDTO);
                System.out.println("El cambio de estado guardado es: " + cambioEstadoGuardado);

                // Creando un nuevo objeto cambio de estado que apunta al estado Rechazado
                CambioEstadoDTO cambioEstadoRechazado = new CambioEstadoDTO();
                cambioEstadoRechazado.setFechaHoraInicio(fechaHoraActual);
                cambioEstadoRechazado.setEventoSismico(eventoSismicoSeleccionadoDTO);

                // Asignando al responsable de inspeccion
                cambioEstadoRechazado.setResponsable(responsableDeInspeccionDTO);

                Long estadoId = estadoConfirmadoDTO.getId();
                System.out.println(" -----__---------> EL ID DEL ESATDO ES: ---> " + estadoId);

                // Verificando que el estado existe y tiene un id asignado 
                if (estadoId == null) {
                    throw new RuntimeException("El DTO de Estado no contiene un ID válido.");
                }

                // Asignando el puntero del estado Rechazado al nuevo cambio de estado creado
                cambioEstadoRechazado.setEstado(estadoConfirmadoDTO);

                System.out.println("Estado persistido asignado al cambio de estado: " + cambioEstadoRechazado.getEstado().getId());

                // Creando el CambioDeEstado a partir del DTO
                cambioEstadoService.crearDesdeDTO(cambioEstadoRechazado);

                break;
            }
        }
    }

    @Transactional(readOnly = true)
    public EventoSismico obtenerEntidadDesdeDTO(EventoSismicoDTO dto) {
        return eventoSismicoRepository.findByIdWithDetails(dto.getId())
            .orElseThrow(() -> new RuntimeException("EventoSismico no encontrado con id: " + dto.getId()));
    }
}