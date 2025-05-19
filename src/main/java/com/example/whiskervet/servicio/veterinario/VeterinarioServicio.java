package com.example.whiskervet.servicio.veterinario;

import java.util.List;

import com.example.whiskervet.entity.Veterinario;

public interface VeterinarioServicio {
    List<Veterinario> obtenerTodosLosVeterinarios();
    Veterinario obtenerVeterinarioPorCedula(Long cedula);
    Veterinario guardarVeterinario(Veterinario veterinario);
    void eliminarVeterinario(Long cedula);
    boolean existeVeterinarioPorCedula(Long cedula);
    Long obtenerIdUltimoVeterinarioCreado() ;
    Long obtenerVeterinariosActivos();
    Long obtenerVeterinariosInactivos();
    void activarVeterinario(Long cedula);
    List<Veterinario> filtrarVeterinariosCedula(Long cedula);
    List<Veterinario> filtrarVeterinariosNombre(String nombre);
    
}