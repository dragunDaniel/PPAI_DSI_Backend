package com.grupo7.application.service;

// Dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Repositorios
import com.grupo7.application.repository.UsuarioRepository;

// Entidades
import com.grupo7.application.entity.Usuario;
import com.grupo7.application.entity.Empleado;

// Mappers
import com.grupo7.application.mapper.UsuarioMapper;
import com.grupo7.application.mapper.EmpleadoMapper;

// DTOs
import com.grupo7.application.dto.UsuarioDTO;
import com.grupo7.application.dto.EmpleadoDTO;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EmpleadoMapper empleadoMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          EmpleadoMapper empleadoMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.empleadoMapper = empleadoMapper;
    }

    public EmpleadoDTO obtenerEmpleadoActual() {
        Usuario usuario = usuarioRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Empleado empleado = usuario.getEmpleado();
        if (empleado == null) {
            throw new RuntimeException("El usuario no tiene empleado asociado");
        }
        return empleadoMapper.toDTO(empleado);
    }

    /**
     * Devuelve el EmpleadoDTO asociado al usuario.
     *
     * @param idUsuario el ID del usuario
     * @return EmpleadoDTO correspondiente
     */
    public EmpleadoDTO obtenerEmpleado(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Empleado empleado = usuario.getEmpleado();
        if (empleado == null) {
            throw new RuntimeException("El usuario no tiene empleado asociado");
        }
        return empleadoMapper.toDTO(empleado);
    }

    /**
     * Devuelve el DTO del usuario.
     *
     * @param idUsuario el ID del usuario
     * @return UsuarioDTO correspondiente
     */
    public UsuarioDTO obtenerUsuarioDTO(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toDTO(usuario);
    }
}
