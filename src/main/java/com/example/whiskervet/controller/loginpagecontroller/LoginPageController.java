package com.example.whiskervet.controller.loginpagecontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.repository.RepositorioUsuarios;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/loginUsuario")
public class LoginPageController {

    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    @GetMapping("/{cedula}")
    public boolean loginClientes(@PathVariable("cedula") Long cedula) {
        List<Usuario> usuarios = repositorioUsuarios.findByCedula(cedula);
        if (!usuarios.isEmpty()) {
            //Usuario usuario = usuarios.get(0);
            return true;
        }
        return false;
    }
}
