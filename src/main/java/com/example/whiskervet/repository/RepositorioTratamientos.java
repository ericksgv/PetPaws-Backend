package com.example.whiskervet.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Tratamiento;

@Repository
public interface RepositorioTratamientos extends JpaRepository<Tratamiento, Long> {
    // Consulta para obtener los tratamientos del último mes
    @Query("SELECT t FROM Tratamiento t WHERE t.fecha >= :startDate AND t.fecha <= :endDate")
    List<Tratamiento> findTratamientosDelUltimoMes(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Tratamiento t WHERE t.medicamento = :medicamento")
    int contarTratamientosPorMedicamento(@Param("medicamento") Medicamento medicamento);

        // Consulta para obtener tratamientos por ID de veterinario
        @Query("SELECT t FROM Tratamiento t WHERE t.veterinario.id = :veterinarioId")
        List<Tratamiento> findTratamientosByVeterinarioId(@Param("veterinarioId") Long veterinarioId);
}

