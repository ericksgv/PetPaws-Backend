package com.example.whiskervet.servicio.mascota;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.repository.RepositorioMascotas;

@Validated
@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private RepositorioMascotas repositorioMascotas;

    @Override
    public Mascota obtenerMascotaPorId(Long id) {
        return repositorioMascotas.findById(id).orElse(null);
    }

    @Override
    public List<Mascota> obtenerTodasLasMascotas() {
        return repositorioMascotas.findAll();
    }

    @Override
    public void eliminarMascotaPorId(Long id) {
        Mascota m = repositorioMascotas.findById(id).orElse(null);
        m.setEstado("Inactivo");
        repositorioMascotas.save(m);
    }

    @Override
    public Mascota guardarMascota(Mascota mascota) {
        return repositorioMascotas.save(mascota);
    }

    @Override
    public void agregarTratamiento(Long id, Tratamiento tratamiento) {
        Mascota m = repositorioMascotas.findById(id).orElse(null);
        m.getTratamientos().add(tratamiento);
        repositorioMascotas.save(m);
    }

    @Override
    public Long obtenerTotalMascotas() {
        return repositorioMascotas.count();
    }

    @Override
    public Long obtenerMascotasTratamiento() {
        return repositorioMascotas.countByEstado("activo");
    }

    @Override
    public void activarMascota(Long id) {
        Mascota m = repositorioMascotas.findById(id).orElse(null);
        m.setEstado("activo");
        repositorioMascotas.save(m);
    }

        @Override
    public List<Mascota> filtrarMascotasPorNombre(String nombre) {
        return repositorioMascotas.findByNombreContaining(nombre.toUpperCase());
    }

}


