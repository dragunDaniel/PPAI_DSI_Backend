package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.ClasificacionSismo;

// DTO
import com.grupo7.application.dto.ClasificacionSismoDTO;

@Mapper(componentModel = "spring")
public interface ClasificacionSismoMapper {
    ClasificacionSismoDTO toDTO(ClasificacionSismo entidad);
    ClasificacionSismo toEntity(ClasificacionSismoDTO dto);
}