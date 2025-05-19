package com.example.whiskervet.dto;

import lombok.Data;

@Data
public class VeterinarioDTO {
    Long id;
    Long cedula;
    String nombre;
    String especialidad;
    int numeroAtenciones;
    String foto;
    String estado;
}
