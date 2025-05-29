package com.grupo7.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.grupo7.application.entity.CambioEstado;
import com.grupo7.application.dto.CambioEstadoDTO;

@Mapper(componentModel = "spring", uses = {
    EstadoMapper.class,
    EmpleadoMapper.class,
    EventoSismicoMapper.class
})
public interface CambioEstadoMapper {

    CambioEstadoDTO toDTO(CambioEstado entity);

    @Mapping(target = "id", ignore = true) // Evita problemas con ID auto-generado
    CambioEstado toEntity(CambioEstadoDTO dto);
}
