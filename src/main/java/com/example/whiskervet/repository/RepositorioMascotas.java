package com.example.whiskervet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.whiskervet.entity.Mascota;

@Repository
public interface RepositorioMascotas extends JpaRepository<Mascota, Long> {

    Long countByEstado(String string);

    @Query("SELECT m FROM Mascota m WHERE UPPER(m.nombre) LIKE UPPER(CONCAT('%', :nombre, '%'))")
List<Mascota> findByNombreContaining(String nombre);
}
