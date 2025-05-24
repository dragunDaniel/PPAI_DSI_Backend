package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.DetalleMuestraSismica;

// DTO
import com.grupo7.application.dto.DetalleMuestraSismicaDTO;

@Mapper(componentModel = "spring")
public interface DetalleMuestraSismicaMapper {
    DetalleMuestraSismicaDTO toDTO(DetalleMuestraSismica entidad);
    DetalleMuestraSismica toEntity(DetalleMuestraSismicaDTO dto);
}