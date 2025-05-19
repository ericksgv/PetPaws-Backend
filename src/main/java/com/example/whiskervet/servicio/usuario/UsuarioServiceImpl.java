package com.example.whiskervet.servicio.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.UsuarioCounter;
import com.example.whiskervet.repository.RepositorioUsuarios;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    @Autowired
    private UsuarioCounter usuarioCounter; 
    @Override
    public List<Usuario> obtenerTodosLosUsuarios() {
        return repositorioUsuarios.findAll();
    }
    @Override
    public List<Usuario> obtenerUsuariosPorCedula(Long cedula) {
        return repositorioUsuarios.findByCedula(cedula);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCedula(Long cedula) {
        return repositorioUsuarios.findUByCedula(cedula);
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        usuarioCounter.getNextId(); // Obtiene el próximo ID del contador global
        return repositorioUsuarios.save(usuario);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario u = repositorioUsuarios.findById(id).orElse(null);
        u.setEstado("inactivo");
        repositorioUsuarios.save(u);
    }

    @Override
    public boolean existeUsuarioPorCedula(Long cedula) {
         
        return repositorioUsuarios.existsByCedula(cedula);
    }
    
    @Override
    public Long obtenerIdUltimoUsuarioCreado() {
        return usuarioCounter.getCurrentId(); // Obtiene el ID actual del contador global
    }
    @Override
    public void activarUsuario(Long id) {
        Usuario u = repositorioUsuarios.findById(id).orElse(null);
        u.setEstado("activo");
        repositorioUsuarios.save(u);
    }
    
    @Override
    public List<Usuario> filtrarUsuariosPorCedula(Long cedula) {
        String cedulaStr = String.valueOf(cedula); // Convierte a String
        return repositorioUsuarios.findByCedulaContaining(cedulaStr);
    }

    @Override
    public Long obtenerTotalUsuarios() {
        return repositorioUsuarios.count();
    }

}
