package com.example.whiskervet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    Long id;
    Long cedula;
    String nombre;
    String correo;
    String estado;
    Long celular;
}