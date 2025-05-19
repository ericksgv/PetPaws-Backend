package com.example.whiskervet.servicio.cita;

import java.util.List;

import java.util.Date;

import com.example.whiskervet.dto.CitasDTO;
import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Usuario;

public interface CitaService {
    List<Citas> getAllCitas();
    Citas getCitaById(Long id);
    void saveCita(Citas cita);
    void deleteCita(Long id);
    List<Date> getHorasDisponiblesParaDia(Date fecha);
    boolean esHoraDisponible(Date horaActual, Date fecha);
    List<Citas> buscarCitasPorIdMascota(Long idMascota);
    List<CitasDTO> buscarCitasPorIdCliente(Long idCliente);
    List<CitasDTO> getCitasDelDiaActual();
}
