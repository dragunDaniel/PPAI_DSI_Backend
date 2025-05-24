package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.Empleado;

// DTO
import com.grupo7.application.dto.EmpleadoDTO;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {
    EmpleadoDTO toDTO(Empleado entidad);
    Empleado toEntity(EmpleadoDTO dto);
}