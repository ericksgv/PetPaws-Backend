package com.example.whiskervet.servicio.medicamento;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.repository.RepositorioMedicamentos;

@Service
public class MedicamentoServiceImpl implements MedicamentoService {

    @Autowired
    private RepositorioMedicamentos repositorioMedicamentos;

    @Override
    public List<Medicamento> obtenerTodosLosMedicamentos() {
        return repositorioMedicamentos.findAll();
    }

    @Override
    public Optional<Medicamento> obtenerMedicamentoPorId(Long id) {
        return repositorioMedicamentos.findById(id);
    }

    @Override
    public Medicamento guardarMedicamento(Medicamento medicamento) {
        return repositorioMedicamentos.save(medicamento);
    }

    @Override
    public void eliminarMedicamento(Long id) {
        repositorioMedicamentos.deleteById(id);
    }

    @Override
    public boolean existeMedicamentoPorId(Long id) {
        return repositorioMedicamentos.existsById(id);
    }

    @Override
    public float obtenerVentasTotales() {
        return repositorioMedicamentos.calcularVentasTotales();
    }

    @Override
    public float obtenerGananciasTotales() {
        return repositorioMedicamentos.calcularGananciasTotales();
    }

    @Override
    public List<Medicamento> obtenerTop3MasVendidos() {
        // Crea un objeto Sort para ordenar por unidadesVendidas en orden descendente
        Sort sort = Sort.by(Sort.Order.desc("unidadesVendidas"));

        // Lista de medicamentos ordenados por unidadesVendidas
        List<Medicamento> topMedicamentos = repositorioMedicamentos.findAll(sort);
        return topMedicamentos.subList(0, 3);
    }

    @Override
    public List<Medicamento> filtrarMedicamentosPorNombre(String nombre) {
        return repositorioMedicamentos.findByNombreContaining(nombre.toUpperCase());
    }

    public List<Medicamento> obtenerTodosLosMedicamentosDisponibles() {
        return repositorioMedicamentos.findByUnidadesDisponiblesGreaterThanZero();
    }

}
