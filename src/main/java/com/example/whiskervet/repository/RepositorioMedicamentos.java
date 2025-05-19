package com.example.whiskervet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.whiskervet.entity.Medicamento;

@Repository
public interface RepositorioMedicamentos extends JpaRepository<Medicamento, Long> {
    @Query(value = "SELECT SUM(m.precioVenta * m.unidadesVendidas) FROM Medicamento m")
    Float calcularVentasTotales();

    @Query("SELECT SUM(m.unidadesVendidas * (m.precioVenta - m.precioCompra)) FROM Medicamento m")
    Float calcularGananciasTotales();

    @Query("SELECT m FROM Medicamento m WHERE UPPER(m.nombre) LIKE UPPER(CONCAT('%', :nombre, '%'))")
    List<Medicamento> findByNombreContaining(String nombre);

    @Query("SELECT m FROM Medicamento m WHERE m.unidadesDisponibles > 0")
    List<Medicamento> findByUnidadesDisponiblesGreaterThanZero();

}

