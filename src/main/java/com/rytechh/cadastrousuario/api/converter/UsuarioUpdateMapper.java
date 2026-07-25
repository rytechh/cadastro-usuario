package com.rytechh.cadastrousuario.api.converter;

import com.rytechh.cadastrousuario.api.request.UsuarioRequestDTO;
import com.rytechh.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioUpdateMapper {

    UsuarioUpdateMapper INSTANCE = Mappers.getMapper(UsuarioUpdateMapper.class);

    UsuarioEntity updateUsuarioFromDTO(UsuarioRequestDTO usuarioDTO, @MappingTarget UsuarioEntity usuarioEntity);
}