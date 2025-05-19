package com.example.whiskervet.controller.loginpagecontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioVeterinarios;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicio;

@RestController
@RequestMapping("/loginVeterinario")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginPageVeterinarioController {

    @Autowired
    VeterinarioServicio veterinarioService;

    @Autowired
    RepositorioVeterinarios repositorioVeterinarios;

    @PostMapping("/login")
    public ResponseEntity<Veterinario> loginVeterinarios(@RequestBody Map<String, String> request) {
        //Long cedula = Long.parseLong(request.get("cedula"));
        String password = request.get("password");
        System.out.println("Contraseña: " + password);
        Veterinario veterinario = veterinarioService.obtenerVeterinarioPorCedula(Long.valueOf(request.get("cedula")));
        if (veterinario == null) {
            return new ResponseEntity<>(null,  HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Veterinario>(veterinario, HttpStatus.BAD_REQUEST);
    }
}

/*
 * 
 *         if (veterinario != null && !veterinario.getEstado().equals("activo")) {
            System.out.println("inactive");
                return "inactive";
            }

        if (veterinario != null && veterinario.checkPassword(password)) {
            if (veterinario.getEstado().equals("activo")) {
                System.out.println("success");
                return "success";
            } 
        }
        System.out.println("incorrect");
        return "incorrect";
    }
 */