package com.grupo7.application.mapper;

// Dependencies
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// Entidades
import com.grupo7.application.entity.Usuario;

// DTOs
import com.grupo7.application.dto.UsuarioDTO;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO dto);
}
