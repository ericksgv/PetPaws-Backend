package com.example.whiskervet.dto;

import com.example.whiskervet.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {
    private String nombre;
    private String raza;
    private int edad;
    private double peso;
    private Usuario dueño;
}
