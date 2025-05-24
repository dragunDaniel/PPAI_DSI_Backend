package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; 

// Entity
import com.grupo7.application.entity.CambioEstado;

// DTO
import com.grupo7.application.dto.CambioEstadoDTO;

@Mapper(componentModel = "spring")
public interface CambioEstadoMapper {
    @Mapping(source = "estado.id", target = "estadoId")
    CambioEstadoDTO toDTO(CambioEstado cambioEstado);

    CambioEstado toEntity(CambioEstadoDTO dto);
}
