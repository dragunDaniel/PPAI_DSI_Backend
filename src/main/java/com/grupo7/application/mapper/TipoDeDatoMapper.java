package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.TipoDeDato;

// DTO
import com.grupo7.application.dto.TipoDeDatoDTO;

@Mapper(componentModel = "spring")
public interface TipoDeDatoMapper {
    TipoDeDatoDTO toDTO(TipoDeDato entidad);
    TipoDeDato toEntity(TipoDeDatoDTO dto);
}