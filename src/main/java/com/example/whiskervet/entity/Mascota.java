package com.example.whiskervet.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Mascota {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String raza;


    private Integer edad;

    private Float peso;

    private String foto;

    private String enfermedad;


    private String estado = "Sin estado";

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "duenio_id")
    private Usuario duenio;

    @JsonIgnore
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.PERSIST)
    private List<Tratamiento> tratamientos = new ArrayList<>();

    public Mascota(String nombre, String raza, int edad, float peso, String enfermedad, String estado, Usuario duenio) {
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.peso = peso;
        this.enfermedad = enfermedad;
        this.estado = estado;
        this.duenio = duenio;
    }
}
