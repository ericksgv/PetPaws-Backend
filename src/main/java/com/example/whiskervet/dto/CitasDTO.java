package com.example.whiskervet.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CitasDTO {
        private Long id;
        private String fechaHora;
    private Long idMascota;
    private String nombreMascota;
}



