package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;

// Entity
import com.grupo7.application.entity.AlcanceSismo;

// DTO
import com.grupo7.application.dto.AlcanceSismoDTO;

@Mapper(componentModel = "spring")
public interface AlcanceSismoMapper {
    AlcanceSismoDTO toDTO(AlcanceSismo entidad);
    AlcanceSismo toEntity(AlcanceSismoDTO dto);
}