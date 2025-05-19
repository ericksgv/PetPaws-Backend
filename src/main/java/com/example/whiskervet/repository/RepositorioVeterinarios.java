    package com.example.whiskervet.repository;

    import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.whiskervet.entity.Veterinario;

    @Repository
    public interface RepositorioVeterinarios extends JpaRepository<Veterinario, Long> {
        
        Veterinario findByCedula(Long cedula);
        boolean existsByCedula(Long cedula);
        Long countByEstado(String estado);
        @Query("SELECT v FROM Veterinario v WHERE CAST(v.cedula AS string) LIKE %:cedula%")
        List<Veterinario> findByCedulaContaining(@Param("cedula") String cedula);

         @Query("SELECT v FROM Veterinario v WHERE LOWER(v.nombre) LIKE LOWER(concat('%', :nombre, '%'))")
    List<Veterinario> findByNombreContaining(@Param("nombre") String nombre);
}