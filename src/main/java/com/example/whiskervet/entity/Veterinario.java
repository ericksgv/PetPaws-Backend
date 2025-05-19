package com.example.whiskervet.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.CascadeType;

@Entity
@Data
@NoArgsConstructor
public class Veterinario {


    @OneToOne
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    Long cedula;
    String nombre;
    
    @Transient
    String passwordHash;
    String especialidad;
    int numeroAtenciones;
    String foto;
    String estado;
    
    public Veterinario (Long cedula, String nombre, String passwordHash, String especialidad, int numeroAtenciones, String foto, String estado){
        this.cedula = cedula;
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        this.especialidad = especialidad;
        this.numeroAtenciones = numeroAtenciones;
        this.foto = foto;
        this.estado = estado;
    }

        public Veterinario (Long cedula, String nombre, String especialidad, int numeroAtenciones, String foto, String estado, String plainPassword){
        this.cedula = cedula;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.numeroAtenciones = numeroAtenciones;
        this.foto = foto;
        this.estado = estado;
        setPasswordHash(plainPassword);
    }

    @JsonIgnore
    @OneToMany(mappedBy = "veterinario")
    private List<Tratamiento> tratamientos = new ArrayList<>();

}
