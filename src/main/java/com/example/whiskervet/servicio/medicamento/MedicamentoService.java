package com.example.whiskervet.servicio.medicamento;

import java.util.List;
import java.util.Optional;

import com.example.whiskervet.entity.Medicamento;

public interface MedicamentoService {
    List<Medicamento> obtenerTodosLosMedicamentos();
    Optional<Medicamento> obtenerMedicamentoPorId(Long id);
    Medicamento guardarMedicamento(Medicamento Medicamento);
    void eliminarMedicamento(Long id);
    boolean existeMedicamentoPorId(Long id);
    public float obtenerVentasTotales();
    public float obtenerGananciasTotales();
    public List<Medicamento> obtenerTop3MasVendidos();
    List<Medicamento> filtrarMedicamentosPorNombre(String nombre);
    public List<Medicamento> obtenerTodosLosMedicamentosDisponibles();
}
