package com.example.whiskervet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Usuario;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface RepositorioCitas extends JpaRepository<Citas, Long>{
    
    boolean existsByFechaHoraBetweenAndFechaHoraBefore(Timestamp inicio, Timestamp fin, java.util.Date fecha);
    @Query("SELECT c FROM Citas c WHERE c.idMascota = :mascotaId")
    List<Citas> findByMascotaId(@Param("mascotaId") Long mascotaId);
    List<Citas> findByFechaHoraAfterAndFechaHoraBefore(Date inicio, Date fin);
}
