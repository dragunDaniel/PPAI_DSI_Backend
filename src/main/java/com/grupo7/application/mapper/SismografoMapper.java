package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.Sismografo;

// DTO
import com.grupo7.application.dto.SismografoDTO;

@Mapper(componentModel = "spring")
public interface SismografoMapper {
    SismografoDTO toDTO(Sismografo entidad);
    Sismografo toEntity(SismografoDTO dto);
}