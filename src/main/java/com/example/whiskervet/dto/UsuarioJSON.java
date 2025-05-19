package com.example.whiskervet.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.whiskervet.entity.Mascota;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioJSON {
    Long cedula;
    String nombre;
    String correo;
    Long celular;
    private List<Mascota> mascotas = new ArrayList<>();
}