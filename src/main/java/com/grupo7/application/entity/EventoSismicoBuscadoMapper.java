package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

// DTOs
import com.grupo7.application.dto.EventoSismicoBuscadoDTO;
import com.grupo7.application.dto.EventoSismicoDTO;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventoSismicoBuscadoMapper {

    /**
     * Mappea desde el DTO completo de búsqueda a un DTO reducido.
     * Solo se copian los campos comunes: id, fechaHoraOcurrencia y coordenadas.
     */
    @Mapping(target = "fechaHoraOcurrencia", source = "fechaHoraOcurrencia")
    @Mapping(target = "latitudEpicentro", source = "latitudEpicentro")
    @Mapping(target = "longitudEpicentro", source = "longitudEpicentro")
    @Mapping(target = "latitudHipocentro", source = "latitudHipocentro")
    @Mapping(target = "longitudHipocentro", source = "longitudHipocentro")
    EventoSismicoDTO toBasicDTO(EventoSismicoBuscadoDTO buscadoDTO);
}
