package com.example.whiskervet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.whiskervet.entity.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositorioUsuarios extends JpaRepository<Usuario, Long> {
    List<Usuario> findByCedula(Long cedula);
    boolean existsByCedula(Long cedula);
    Optional<Usuario> findUByCedula(Long cedula);
        // Método para filtrar usuarios por cédula
        @Query("SELECT u FROM Usuario u WHERE CAST(u.cedula AS string) LIKE %:cedula%")
    List<Usuario> findByCedulaContaining(@Param("cedula") String cedula);
}
