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
public class TratamientoInfoDTO {
    String descripcion;
    String nombreMedicamento;
    LocalDate fecha;
    String nombreVeterinario;
}
