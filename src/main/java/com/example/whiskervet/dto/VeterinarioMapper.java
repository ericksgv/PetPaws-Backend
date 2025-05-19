package com.example.whiskervet.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.whiskervet.entity.Veterinario;

@Mapper
public interface VeterinarioMapper {
    VeterinarioMapper INSTANCE = Mappers.getMapper(VeterinarioMapper.class);
    VeterinarioDTO convert(Veterinario veterinario);
}