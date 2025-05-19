package com.example.whiskervet.servicio.mascota;

import java.util.List;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Tratamiento;


public interface MascotaService {

    Mascota obtenerMascotaPorId(Long id);

    List<Mascota> obtenerTodasLasMascotas();

    void eliminarMascotaPorId(Long id);

    void agregarTratamiento(Long id, Tratamiento tratamiento);

    Mascota guardarMascota(Mascota mascota);

    public Long obtenerTotalMascotas();

    public Long obtenerMascotasTratamiento();

    public void activarMascota(Long id);

        public List<Mascota> filtrarMascotasPorNombre(String nombre);
}

