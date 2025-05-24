package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.SerieTemporal;

// DTO
import com.grupo7.application.dto.SerieTemporalDTO;

@Mapper(componentModel = "spring")
public interface SerieTemporalMapper {
    SerieTemporalDTO toDTO(SerieTemporal entidad);
    SerieTemporal toEntity(SerieTemporalDTO dto);
}