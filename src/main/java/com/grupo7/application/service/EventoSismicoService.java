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

// Services
import com.grupo7.application.service.CambioEstadoService;
import com.grupo7.application.service.EstadoService;
import com.grupo7.application.service.SerieTemporalService;
import com.grupo7.application.service.DetalleMuestraSismicaService;

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
        CambioEstadoDTO actual = cambioEstadoService.obtenerTodosDTO().stream()
            .filter(ce -> ce.getEventoSismico() != null
                       && ce.getEventoSismico().getId().equals(evento.getId())
                       && ce.esEstadoActual())
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                "No existe cambio de estado activo para evento " + evento.getId()));

        actual.setFechaHoraFin(fechaHora);
        cambioEstadoService.actualizarDesdeDTO(actual.getId(), actual);

        CambioEstadoDTO nuevo = new CambioEstadoDTO();
        nuevo.setEventoSismico(evento);
        nuevo.setFechaHoraInicio(fechaHora);
        nuevo.setEstado(estadoDestino);
        nuevo.setResponsable(responsable);
        cambioEstadoService.crearDesdeDTO(nuevo);
    }

    //Cambios de estado

    //Cambiar estado a rechazado
    public void rechazarEventoSismico(EventoSismicoDTO evento, EmpleadoDTO responsable, LocalDateTime fechaHoraActual, EstadoDTO estadoRechazado) {
        cambiarEstado(evento, fechaHoraActual, estadoRechazado, responsable);
    }

    //Cambiar estado a BloqueadoEnRevision
    public void bloquearPorRevision(EventoSismicoDTO evento, EmpleadoDTO responsable, LocalDateTime fechaHoraActual, EstadoDTO estadoBloqueado) {
        cambiarEstado(evento, fechaHoraActual, estadoBloqueado, responsable);
    }

    //Cambiar estado a confirmado
    public void confirmar(EventoSismicoDTO evento, EmpleadoDTO responsable, LocalDateTime fechaHoraActual, EstadoDTO estadoDerivadoAExperto) {
        cambiarEstado(evento, fechaHoraActual, estadoDerivadoAExperto, responsable);
    }

    //Cambiar estado a DerivadoAExperto
    public void derivarAExperto(EventoSismicoDTO evento, EmpleadoDTO responsable, LocalDateTime fechaHoraActual, EstadoDTO estadoDerivadoAExperto) {
        cambiarEstado(evento, fechaHoraActual, estadoDerivadoAExperto, responsable);
    }

    
    public boolean esAutoDetectadoOPendienteDeRevision(Long estadoId) {
        // These methods (sosAutoDetectado, sosPendienteDeRevision) should be in CambioEstadoService
        return cambioEstadoService.sosAutoDetectado(estadoId) ||
               cambioEstadoService.sosPendienteDeRevision(estadoId);
    }

    public List<EventoSismico> obtenerTodosLosEventosSismicos() {
        return eventoSismicoRepository.findAll(); // Assuming you have a repository
    }

    // Obtener datos principales de los eventos sismicos
    public List<EventoSismicoDTO> obtenerDatosPrincipales(List<EventoSismicoBuscadoDTO> buscados) {
        return buscados.stream()
            .map(buscadoDTO -> {
                EventoSismicoDTO eventoSismicoDTO = new EventoSismicoDTO();
                eventoSismicoDTO.setId(buscadoDTO.getId());
                eventoSismicoDTO.setFechaHoraOcurrencia(buscadoDTO.getFechaHoraOcurrencia());
                eventoSismicoDTO.setLatitudEpicentro(buscadoDTO.getLatitudEpicentro());
                eventoSismicoDTO.setLongitudEpicentro(buscadoDTO.getLongitudEpicentro());
                eventoSismicoDTO.setLatitudHipocentro(buscadoDTO.getLatitudHipocentro());
                eventoSismicoDTO.setLongitudHipocentro(buscadoDTO.getLongitudHipocentro());
                eventoSismicoDTO.setValorMagnitud(buscadoDTO.getValorMagnitud());
                return eventoSismicoDTO;
            })
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
    
        // 3. Process series temporales using the new method in SerieTemporalService
        List<SerieTemporalDetalleDTO> seriesTemporalesDetallesDTOs = new ArrayList<>();
    
        if (eventoSismicoEntidad.getSeriesTemporales() != null) {
            seriesTemporalesDetallesDTOs = eventoSismicoEntidad.getSeriesTemporales().stream()
                .map(serieTemporalService::getDatos) // Calls the new getDatos method in SerieTemporalService
                .collect(Collectors.toList());
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