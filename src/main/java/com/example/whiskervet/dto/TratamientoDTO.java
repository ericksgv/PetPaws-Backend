package com.example.whiskervet.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TratamientoDTO {
    private String descripcion;
    private LocalDate fecha;
    private long idMascota;
    private long cedulaVeterinario;
    private long idMedicamento;
}