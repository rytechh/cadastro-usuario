package com.rytechh.cadastrousuario.api.converter;

import com.rytechh.cadastrousuario.api.response.UsuarioResponseDTO;
import com.rytechh.cadastrousuario.infrastructure.entities.UsuarioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO paraUsuarioResponseDTO(UsuarioEntity usuario);

}
