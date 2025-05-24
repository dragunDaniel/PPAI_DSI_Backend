package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.Estado;

// DTO
import com.grupo7.application.dto.EstadoDTO;

@Mapper(componentModel = "spring")
public interface EstadoMapper {
    EstadoDTO toDTO(Estado entidad);
    Estado toEntity(EstadoDTO dto);
}