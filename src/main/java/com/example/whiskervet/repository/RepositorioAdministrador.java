package com.example.whiskervet.repository;

import org.springframework.stereotype.Repository;

import com.example.whiskervet.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface RepositorioAdministrador extends JpaRepository<Administrador, Long> {
    Administrador findByCedula(Long cedula);
}

