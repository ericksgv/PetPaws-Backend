package com.example.whiskervet.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "clientes", uniqueConstraints = @UniqueConstraint(columnNames = {"cedula"}))
public class Usuario {
    
    @OneToOne
    private UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, unique = true)
    Long cedula;
    @Column(nullable = false)
    String nombre;
    @Column(nullable = false)
    String correo;
    @Column(nullable = false)
    Long celular;

    @Column(nullable = false)
    String estado;

    @JsonIgnore
    @OneToMany(mappedBy = "duenio", cascade = CascadeType.PERSIST)
    private List<Mascota> mascotas = new ArrayList<>();

    public Usuario(Long cedula, String nombre, String correo, Long celular, String estado, List<Mascota> mascotas) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.celular = celular;
        this.estado = estado;
        this.mascotas = mascotas;
    }
    
 
}
