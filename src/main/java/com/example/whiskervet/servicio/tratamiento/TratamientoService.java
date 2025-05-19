package com.example.whiskervet.servicio.tratamiento;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Tratamiento;

public interface TratamientoService {
    List<Tratamiento> obtenerTodosLosTratamientos();
    Optional<Tratamiento> obtenerTratamientoPorId(Long id);
    Tratamiento guardarTratamiento(Tratamiento Tratamiento);
    void eliminarTratamiento(Long id);
    boolean existeTratamientoPorId(Long id);
    public List<Tratamiento> obtenerTratamientosDelUltimoMes();
    HashMap<String, Integer> cantidadTratamientosporMedicamentoUltimoMes();
    List<Tratamiento> obtenerTratamientosPorVeterinario(Long id);
    List<Mascota> obtenerMascotasAtendidasPorVeterinario(Long id);
}
