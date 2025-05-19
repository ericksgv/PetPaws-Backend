package com.example.whiskervet.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.whiskervet.entity.Usuario;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    UsuarioDTO convert(Usuario usuario);
}
