package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.MuestraSismica;

// DTO
import com.grupo7.application.dto.MuestraSismicaDTO;

@Mapper(componentModel = "spring")
public interface MuestraSismicaMapper {
    MuestraSismicaDTO toDTO(MuestraSismica entidad);
    MuestraSismica toEntity(MuestraSismicaDTO dto);
}