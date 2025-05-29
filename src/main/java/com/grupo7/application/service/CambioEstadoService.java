package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importar para control transaccional
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para operaciones de stream

// Entities
import com.grupo7.application.entity.CambioEstado;
import com.grupo7.application.entity.EventoSismico;

// Repositories
import com.grupo7.application.repository.CambioEstadoRepository;

// DTOs
import com.grupo7.application.dto.CambioEstadoDTO;

// Mappers
import com.grupo7.application.mapper.CambioEstadoMapper;

// Services

@Service
public class CambioEstadoService {

    private final CambioEstadoRepository cambioEstadoRepository;
    private final CambioEstadoMapper cambioEstadoMapper;
    private final EstadoService estadoService; // Se asume que EstadoService es necesario aquí

    @Autowired
    public CambioEstadoService(CambioEstadoRepository cambioEstadoRepository, CambioEstadoMapper cambioEstadoMapper, EstadoService estadoService) {
        this.cambioEstadoRepository = cambioEstadoRepository;
        this.cambioEstadoMapper = cambioEstadoMapper;
        this.estadoService = estadoService;
    }

    /**
     * Obtiene el cambio de estado actual (fechaHoraFin es NULL) para un EventoSismico dado.
     * @param eventoSismico La entidad EventoSismico para la cual buscar el cambio de estado actual.
     * @return El DTO del CambioEstado actual, o null si no se encuentra.
     */
    @Transactional(readOnly = true) // Este método solo lee datos
    public CambioEstadoDTO obtenerCambioEstadoActual(EventoSismico eventoSismico) {
        Optional<CambioEstado> cambioEstadoOpt = cambioEstadoRepository.findByEventoSismicoAndFechaHoraFinIsNull(eventoSismico);
        
        // Para depuración, se puede usar un logger en lugar de System.out.println
        System.out.println("DEBUG: El cambio de estado encontrado es: " + cambioEstadoOpt.orElse(null));

        if (cambioEstadoOpt.isPresent()) {
            return cambioEstadoMapper.toDTO(cambioEstadoOpt.get());
        } else {
            // No hay cambio de estado actual para ese evento
            return null;
        }
    }
    
    /**
     * Verifica si un estado (identificado por su ID) corresponde al estado "AutoDetectado".
     * Delega la verificación a EstadoService.
     * @param idEstado El ID del estado a verificar.
     * @return true si es "AutoDetectado", false en caso contrario.
     */
    @Transactional(readOnly = true) // Este método solo lee datos
    public boolean sosAutoDetectado(Long idEstado) {
        // Se asume que EstadoService.sosAutoDetectado(id) verifica el nombre del estado
        return estadoService.sosAutoDetectado(idEstado);
    }
    
    /**
     * Verifica si un estado (identificado por su ID) corresponde al estado "PendienteDeRevision".
     * Delega la verificación a EstadoService.
     * @param idEstado El ID del estado a verificar.
     * @return true si es "PendienteDeRevision", false en caso contrario.
     */
    @Transactional(readOnly = true) // Este método solo lee datos
    public boolean sosPendienteDeRevision(Long idEstado) {
        // Se asume que EstadoService.sosPendienteDeRevision(id) verifica el nombre del estado
        return estadoService.sosPendienteDeRevision(idEstado);
    }

    /**
     * Obtiene todos los cambios de estado y los mapea a DTOs.
     * @return Una lista de CambioEstadoDTO.
     */
    @Transactional(readOnly = true) // Este método solo lee datos
    public List<CambioEstadoDTO> obtenerTodosDTO() {
        List<CambioEstado> entidades = cambioEstadoRepository.findAll();
        return entidades.stream()
                        .map(cambioEstadoMapper::toDTO)
                        .collect(Collectors.toList()); // Usar collect(Collectors.toList()) en lugar de toList() para compatibilidad
    }
    
    /**
     * Crea un nuevo cambio de estado a partir de un DTO.
     * @param dto El DTO del cambio de estado a crear.
     * @return El DTO del cambio de estado guardado.
     */
    @Transactional // Este método modifica datos
    public CambioEstadoDTO crearDesdeDTO(CambioEstadoDTO dto) {
        CambioEstado entidad = cambioEstadoMapper.toEntity(dto);
        // Asegurarse de que las entidades relacionadas (EventoSismico, Estado, Empleado)
        // en el DTO se conviertan a entidades persistidas antes de guardar.
        // Esto es responsabilidad del mapper o de este servicio.
        // Por ejemplo, si CambioEstadoDTO tiene EventoSismicoDTO,
        // el mapper debería obtener la entidad EventoSismico real del repositorio.
        
        // Si el DTO ya trae la entidad EventoSismico (como en obtenerEntidadDesdeDTO de EventoSismicoService)
        // entonces no es necesario buscarla de nuevo aquí, pero el mapper debe manejar la conversión.
        
        CambioEstado guardado = cambioEstadoRepository.save(entidad);
        return cambioEstadoMapper.toDTO(guardado);
    }
    
    /**
     * Actualiza un cambio de estado existente a partir de un DTO.
     * @param id El ID del cambio de estado a actualizar.
     * @param dto El DTO con los datos actualizados.
     * @return El DTO del cambio de estado actualizado.
     * @throws RuntimeException si el CambioEstado no es encontrado.
     */
    @Transactional // Este método modifica datos
    public CambioEstadoDTO actualizarDesdeDTO(Long id, CambioEstadoDTO dto) {
        return cambioEstadoRepository.findById(id)
            .map(existing -> {
                // El mapper debería actualizar 'existing' con los datos del 'dto'
                // o crear una nueva entidad y copiar los datos.
                // Si el DTO contiene IDs de entidades relacionadas, aquí se deberían
                // buscar las entidades reales de la base de datos y asignarlas a 'entidadActualizada'.
                
                // Ejemplo: Si dto.getEventoSismico() es un EventoSismicoDTO,
                // necesitarías obtener la entidad EventoSismico real:
                // existing.setEventoSismico(eventoSismicoService.obtenerEntidadDesdeDTO(dto.getEventoSismico()));
                // Similar para Estado y Empleado.
                
                CambioEstado entidadActualizada = cambioEstadoMapper.toEntity(dto);
                entidadActualizada.setId(id); // Asegúrate de mantener el id para la actualización
                CambioEstado guardado = cambioEstadoRepository.save(entidadActualizada);
                return cambioEstadoMapper.toDTO(guardado);
            })
            .orElseThrow(() -> new RuntimeException("CambioEstado no encontrado con id: " + id));
    }
}
