package com.example.whiskervet.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Administrador {

    @OneToOne
    @JsonIgnore
    private UserEntity user;

    @Id
    private Long cedula;
    @Transient
    private String password;

}
