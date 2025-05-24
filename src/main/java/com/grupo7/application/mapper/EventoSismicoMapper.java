package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.EventoSismico;

// DTO
import com.grupo7.application.dto.EventoSismicoDTO;

@Mapper(componentModel = "spring")
public interface EventoSismicoMapper {
    EventoSismicoDTO toDTO(EventoSismico entidad);
    EventoSismico toEntity(EventoSismicoDTO dto);
}