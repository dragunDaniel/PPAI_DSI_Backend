package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Entity
import com.grupo7.application.entity.TipoDeDato;

// DTO
import com.grupo7.application.dto.TipoDeDatoDTO;

@Mapper(componentModel = "spring")
public interface TipoDeDatoMapper {

    TipoDeDatoDTO toDTO(TipoDeDato entidad);
    
    
    @Mapping(target = "id", ignore = true) // Fundamental para no violar la clave primaria autogenerada
    TipoDeDato toEntity(TipoDeDatoDTO dto);
}