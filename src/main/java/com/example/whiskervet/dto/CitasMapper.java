package com.example.whiskervet.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.whiskervet.entity.Citas;

@Mapper
public interface CitasMapper {
    CitasMapper INSTANCE = Mappers.getMapper(CitasMapper.class);
    CitasDTO convert(Citas citas);
}
