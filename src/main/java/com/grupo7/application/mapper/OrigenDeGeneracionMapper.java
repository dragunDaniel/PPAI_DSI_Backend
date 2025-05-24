package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.OrigenDeGeneracion;

// DTO
import com.grupo7.application.dto.OrigenDeGeneracionDTO;

@Mapper(componentModel = "spring")
public interface OrigenDeGeneracionMapper {
    OrigenDeGeneracionDTO toDTO(OrigenDeGeneracion entidad);
    OrigenDeGeneracion toEntity(OrigenDeGeneracionDTO dto);
}