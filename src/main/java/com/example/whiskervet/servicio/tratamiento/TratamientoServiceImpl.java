package com.example.whiskervet.servicio.tratamiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioTratamientos;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicioImpl;

@Service
public class TratamientoServiceImpl implements TratamientoService {

    @Autowired
    private RepositorioTratamientos repositorioTratamientos;

    @Autowired
    private VeterinarioServicioImpl veterinarioService;

    @Autowired
    private MedicamentoServiceImpl medicamentoService;

    @Override
    public List<Tratamiento> obtenerTodosLosTratamientos() {
        return repositorioTratamientos.findAll();
    }

    @Override
    public Optional<Tratamiento> obtenerTratamientoPorId(Long id) {
        return repositorioTratamientos.findById(id);
    }

    @Override
    public Tratamiento guardarTratamiento(Tratamiento tratamiento) {
        Veterinario vet = veterinarioService.obtenerVeterinarioPorCedula(tratamiento.getVeterinario().getCedula());
        vet.setNumeroAtenciones(vet.getNumeroAtenciones() + 1);
        veterinarioService.guardarVeterinario(vet);

        Medicamento med = medicamentoService.obtenerMedicamentoPorId(tratamiento.getMedicamento().getId()).orElse(null);
        med.setUnidadesDisponibles(med.getUnidadesDisponibles() - 1);
        med.setUnidadesVendidas(med.getUnidadesVendidas() + 1);
        medicamentoService.guardarMedicamento(med);

        return repositorioTratamientos.save(tratamiento);
    }

    @Override
    public void eliminarTratamiento(Long id) {
        repositorioTratamientos.deleteById(id);
    }

    @Override
    public boolean existeTratamientoPorId(Long id) {
        return repositorioTratamientos.existsById(id);
    }

    @Override
    public List<Tratamiento> obtenerTratamientosDelUltimoMes() {

        LocalDate startDate = LocalDate.now().minusMonths(1);

        LocalDate endDate = LocalDate.now();

        return repositorioTratamientos.findTratamientosDelUltimoMes(startDate, endDate);
    }

    

    @Override
    public HashMap<String, Integer> cantidadTratamientosporMedicamentoUltimoMes() {
        List<Tratamiento> tratamientos = this.obtenerTratamientosDelUltimoMes();

        HashMap<String, Integer> medicamentos = new HashMap<String, Integer>();

        for (Tratamiento tratamiento : tratamientos) {
            Medicamento medicamento = tratamiento.getMedicamento();

            // Verifica si el medicamento ya existe en el mapa
            if (medicamentos.containsKey(medicamento.getNombre())) {
                int cantidadExistente = medicamentos.get(medicamento.getNombre());
                medicamentos.put(medicamento.getNombre(), cantidadExistente + 1);
            } else {
                medicamentos.put(medicamento.getNombre(), 1);
            }
        }

        return medicamentos;
    }

    @Override
    public List<Tratamiento> obtenerTratamientosPorVeterinario(Long id) {
        return repositorioTratamientos.findTratamientosByVeterinarioId(id);
    }

    @Override
    public List<Mascota> obtenerMascotasAtendidasPorVeterinario(Long id) {
        List<Tratamiento> tratamientos = repositorioTratamientos.findTratamientosByVeterinarioId(id);
        List<Mascota> mascotasAtendidas = new ArrayList<>();

        for (Tratamiento tratamiento : tratamientos) {
            Mascota mascota = tratamiento.getMascota();
            if (!mascotasAtendidas.contains(mascota)) {
                mascotasAtendidas.add(mascota);
            }
        }

        return mascotasAtendidas;
    }

}
