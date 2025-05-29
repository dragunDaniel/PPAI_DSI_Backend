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

<<<<<<< HEAD
// Services
import com.grupo7.application.service.CambioEstadoService;
import com.grupo7.application.service.EstadoService;
import com.grupo7.application.service.SerieTemporalService;
import com.grupo7.application.service.DetalleMuestraSismicaService;

=======
>>>>>>> 94e1bc50e1f755a856c5c83850ba678f73fc4667
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
    private final DetalleMuestraSismicaService detalleMuestraSismicaService;

    @Autowired
    public EventoSismicoService(EventoSismicoRepository eventoSismicoRepository, EventoSismicoMapper eventoSismicoMapper,
                                CambioEstadoService cambioEstadoService, EstadoService estadoService, EstadoMapper estadoMapper,
                                SerieTemporalService serieTemporalService, SerieTemporalMapper serieTemporalMapper,
                                DetalleMuestraSismicaMapper detalleMuestraSismicaMapper, SismografoMapper sismografoMapper,
                                MuestraSismicaMapper muestraSismicaMapper, EventoSismicoBuscadoMapper eventoSismicoBuscadoMapper,
                                DetalleMuestraSismicaService detalleMuestraSismicaService) {
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
        this.detalleMuestraSismicaService = detalleMuestraSismicaService;
    }

    private void cambiarEstado(
        EventoSismicoDTO evento,
        LocalDateTime fechaHora,
        EstadoDTO estadoDestino,
        EmpleadoDTO responsable
    ) {
        // 1. Buscar el CambioEstado activo
        CambioEstadoDTO actual = cambioEstadoService.obtenerTodosDTO().stream()
            .filter(ce -> ce.getEventoSismico() != null
                       && ce.getEventoSismico().getId().equals(evento.getId())
                       && ce.esEstadoActual())
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "No existe cambio de estado activo para evento " + evento.getId()));

        // 2. Cerrar el activo
        actual.setFechaHoraFin(fechaHora);
        cambioEstadoService.actualizarDesdeDTO(actual.getId(), actual);

        // 3. Abrir el nuevo
        CambioEstadoDTO nuevo = new CambioEstadoDTO();
        nuevo.setEventoSismico(evento);
        nuevo.setFechaHoraInicio(fechaHora);
        nuevo.setEstado(estadoDestino);
        nuevo.setResponsable(responsable);
        cambioEstadoService.crearDesdeDTO(nuevo);
    }

    /**
     * Pone el evento en “Rechazado”.
     */
    public void rechazar(EventoSismicoDTO evento, EmpleadoDTO responsable) {
        EstadoDTO estadoRechazado = estadoService.obtenerTodosDTO().stream()
            .filter(EstadoDTO::sosRechazado)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Estado Rechazado no encontrado"));
        cambiarEstado(evento, LocalDateTime.now(), estadoRechazado, responsable);
    }

    /**
     * Pone el evento en “BloqueadoEnRevision”.
     */
    public void bloquearPorRevision(EventoSismicoDTO evento, EmpleadoDTO responsable) {
        EstadoDTO estadoBloqueado = estadoService.obtenerTodosDTO().stream()
            .filter(EstadoDTO::sosBloqueadoEnRevision)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Estado BloqueadoEnRevision no encontrado"));
        cambiarEstado(evento, LocalDateTime.now(), estadoBloqueado, responsable);
    }

    /**
     * Pone el evento en “Confirmado”.
     */
    public void confirmar(EventoSismicoDTO evento, EmpleadoDTO responsable) {
        EstadoDTO estadoConfirmado = estadoService.obtenerTodosDTO().stream()
            .filter(EstadoDTO::sosConfirmado)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Estado Confirmado no encontrado"));
        cambiarEstado(evento, LocalDateTime.now(), estadoConfirmado, responsable);
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
                                
                                // Obteniendo la denominacion
                                String denominacion = detalleMuestraSismicaService.getDatos(detalleMuestraSismica.getId());
                                
                                // Asignando la denominacion al detalle al DTO del detalle de la muestra sismica 
                                DetalleMuestraSismicaDTO detalleMuestraSisimcaDTO = detalleMuestraSismicaMapper.toDTO(detalleMuestraSismica);
                                detalleMuestraSisimcaDTO.setDenominacion(denominacion);

                                // Añadiendo el dto del detalle de la muestra simsica al listado de datos válidos
                                detallesMuestraDTOsForMuestra.add(detalleMuestraSisimcaDTO);
                                
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

    @Transactional(readOnly = true)
    public List<EventoSismico> obtenerTodosNoDTO() {
        List<EventoSismico> entidades = eventoSismicoRepository.findAll();
        return entidades;
    }

    @Transactional(readOnly = true)
    public List<EventoSismicoDTO> obtenerTodosDTO() {
        return eventoSismicoRepository.findAll()
            .stream()
            .map(eventoSismicoMapper::toDTO)
            .toList();
    }

    @Transactional
    public EventoSismicoDTO crearDesdeDTO(EventoSismicoDTO dto) {
        EventoSismico entidad = eventoSismicoMapper.toEntity(dto);
        EventoSismico guardado = eventoSismicoRepository.save(entidad);
        return eventoSismicoMapper.toDTO(guardado);
    }

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

    @Transactional(readOnly = true)
    public EventoSismico obtenerEntidadDesdeDTO(EventoSismicoDTO dto) {
        return eventoSismicoRepository.findByIdWithDetails(dto.getId())
            .orElseThrow(() -> new RuntimeException("EventoSismico no encontrado con id: " + dto.getId()));
    }
}