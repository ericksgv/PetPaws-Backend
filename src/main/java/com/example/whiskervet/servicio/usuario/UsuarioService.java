package com.example.whiskervet.servicio.usuario;

import com.example.whiskervet.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> obtenerTodosLosUsuarios();
    Optional<Usuario> obtenerUsuarioPorCedula(Long cedula);
    List<Usuario> obtenerUsuariosPorCedula(Long cedula);
    Usuario guardarUsuario(Usuario usuario);
    void eliminarUsuario(Long cedula);
    boolean existeUsuarioPorCedula(Long cedula);
    Long obtenerIdUltimoUsuarioCreado() ;
    void activarUsuario(Long id);
    List<Usuario> filtrarUsuariosPorCedula(Long cedula);
    Long obtenerTotalUsuarios();
}

