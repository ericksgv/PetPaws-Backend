package com.example.whiskervet.servicio.veterinario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.entity.VeterinarioCounter;
import com.example.whiskervet.repository.RepositorioVeterinarios;

@Service
public class VeterinarioServicioImpl implements VeterinarioServicio{

     @Autowired
    private RepositorioVeterinarios repositorioVeterinarios;

    @Autowired
    private VeterinarioCounter usuarioCounter; 
    @Override
    public List<Veterinario> obtenerTodosLosVeterinarios() {
        return repositorioVeterinarios.findAll();
    }

    @Override
    public Veterinario obtenerVeterinarioPorCedula(Long cedula) {
        return repositorioVeterinarios.findByCedula(cedula);
    }

    @Override
    public Veterinario guardarVeterinario(Veterinario usuario) {
        return repositorioVeterinarios.save(usuario);
    }

    @Override
    public void eliminarVeterinario(Long cedula) {
        List<Veterinario> v = repositorioVeterinarios.findAll();
        for (Veterinario veterinario : v) {
            if (veterinario.getCedula().equals(cedula)) {
                veterinario.setEstado("inactivo");
                repositorioVeterinarios.save(veterinario);
                break;
            }
        }
    }

    @Override
    public boolean existeVeterinarioPorCedula(Long cedula) {
         
        return repositorioVeterinarios.existsByCedula(cedula);
    }
    
    @Override
    public Long obtenerIdUltimoVeterinarioCreado() {
        return usuarioCounter.getCurrentId(); // Obtiene el ID actual del contador global
    }

    @Override
    public Long obtenerVeterinariosActivos() {
        return repositorioVeterinarios.countByEstado("activo");
    }

    @Override
    public Long obtenerVeterinariosInactivos() {
        return repositorioVeterinarios.countByEstado("inactivo");
    }

    @Override
    public void activarVeterinario(Long cedula){
        List<Veterinario> v = repositorioVeterinarios.findAll();
        for (Veterinario veterinario : v) {
            if (veterinario.getCedula().equals(cedula)) {
                veterinario.setEstado("activo");
                repositorioVeterinarios.save(veterinario);
                break;
            }
        }
    }

    @Override
    public List<Veterinario> filtrarVeterinariosCedula(Long cedula) {
        String cedulaStr = String.valueOf(cedula); // Convierte a String
        return repositorioVeterinarios.findByCedulaContaining(cedulaStr);
    }


        @Override
    public List<Veterinario> filtrarVeterinariosNombre(String nombre) {
        return repositorioVeterinarios.findByNombreContaining(nombre);
    }

    
}