package com.grupo7.application.service;

// Dependencies
import org.springframework.stereotype.Service;

// Repositorios
import com.grupo7.application.repository.EmpleadoRepository;

// Entidades
import com.grupo7.application.entity.Empleado;

// Mappers 
import com.grupo7.application.mapper.EmpleadoMapper;

// DTOs
import com.grupo7.application.dto.EmpleadoDTO;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    public EmpleadoService(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoMapper = empleadoMapper;
        this.empleadoRepository = empleadoRepository;
    }

    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public EmpleadoDTO obtenerEmpleadoDTOPorId(Long id) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        if (empleado != null) {
            return empleadoMapper.toDTO(empleado);
        }
        return null;
    }
}
