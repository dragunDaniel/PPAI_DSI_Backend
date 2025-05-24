package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.CambioEstado;

// DTO
import com.grupo7.application.dto.CambioEstadoDTO;

@Mapper(componentModel = "spring")
public interface CambioEstadoMapper {
    CambioEstadoDTO toDTO(CambioEstado entidad);
    CambioEstado toEntity(CambioEstadoDTO dto);
}