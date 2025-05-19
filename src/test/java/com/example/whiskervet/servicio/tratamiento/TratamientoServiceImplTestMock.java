package com.example.whiskervet.servicio.tratamiento;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioTratamientos;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicioImpl;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TratamientoServiceImplTestMock {

    @InjectMocks
    private TratamientoServiceImpl tratamientoService;

    @Mock
    private RepositorioTratamientos repositorioTratamientos;

    @Mock
    private VeterinarioServicioImpl veterinarioService;

    @Mock
    private MedicamentoServiceImpl medicamentoService;

    @Test
    public void obtenerTodosLosTratamientos() {
        List<Tratamiento> tratamientosEjemplo = new ArrayList<>();

        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 10, 1));
        tratamientosEjemplo.add(tratamiento1);

        Tratamiento tratamiento2 = new Tratamiento();
        tratamiento2.setId(2L);
        tratamiento2.setDescripcion("Tratamiento 2");
        tratamiento2.setFecha(LocalDate.of(2023, 10, 5));
        tratamientosEjemplo.add(tratamiento2);

        when(repositorioTratamientos.findAll()).thenReturn(tratamientosEjemplo);

        List<Tratamiento> result = tratamientoService.obtenerTodosLosTratamientos();

        assertEquals(tratamientosEjemplo, result);

        verify(repositorioTratamientos).findAll();
    }

    @Test
    public void obtenerTratamientoPorId() {

        Long tratamientoId = 1L;

        Tratamiento tratamientoEjemplo = new Tratamiento();
        tratamientoEjemplo.setId(tratamientoId);
        tratamientoEjemplo.setDescripcion("Tratamiento de ejemplo");
        tratamientoEjemplo.setFecha(LocalDate.of(2023, 10, 15));

        when(repositorioTratamientos.findById(tratamientoId)).thenReturn(Optional.of(tratamientoEjemplo));

        Optional<Tratamiento> result = tratamientoService.obtenerTratamientoPorId(tratamientoId);

        assertTrue(result.isPresent());
        assertEquals(tratamientoEjemplo, result.get());

        verify(repositorioTratamientos).findById(tratamientoId);
    }

    @Test
    public void testGuardarTratamiento() {

        Tratamiento tratamiento = new Tratamiento();
        tratamiento.setId(1L);
        tratamiento.setDescripcion("Tratamiento de prueba");
        tratamiento.setFecha(LocalDate.of(2023, 10, 1));

        Veterinario veterinario = new Veterinario();
        veterinario.setCedula(12345L);
        veterinario.setNombre("Dr. Veterinario");
        veterinario.setEspecialidad("Especialidad Veterinaria");
        veterinario.setNumeroAtenciones(5);
        veterinario.setFoto("URL de la Foto");
        veterinario.setEstado("Activo");

        Medicamento medicamento = new Medicamento();
        medicamento.setId(3L);
        medicamento.setNombre("Medicamento de Prueba");
        medicamento.setPrecioVenta(10.0f);
        medicamento.setPrecioCompra(5.0f);
        medicamento.setUnidadesDisponibles(20);
        medicamento.setUnidadesVendidas(10);

        tratamiento.setVeterinario(veterinario);
        tratamiento.setMedicamento(medicamento);

        when(veterinarioService.obtenerVeterinarioPorCedula(veterinario.getCedula())).thenReturn(veterinario);
        when(medicamentoService.obtenerMedicamentoPorId(medicamento.getId())).thenReturn(Optional.of(medicamento));

        when(repositorioTratamientos.save(tratamiento)).thenReturn(tratamiento);

        Assertions.assertThat(tratamientoService.guardarTratamiento(tratamiento)).isNotNull();
        verify(veterinarioService, times(1)).obtenerVeterinarioPorCedula(veterinario.getCedula());
        verify(medicamentoService, times(1)).obtenerMedicamentoPorId(medicamento.getId());
        verify(repositorioTratamientos, times(1)).save(tratamiento);
    }

    @Test
    public void obtenerTratamientosDelUltimoMes() {

        List<Tratamiento> tratamientosEjemplo = new ArrayList<>();
        tratamientosEjemplo.add(new Tratamiento());
        tratamientosEjemplo.add(new Tratamiento());

        when(repositorioTratamientos.findTratamientosDelUltimoMes(any(), any())).thenReturn(tratamientosEjemplo);

        List<Tratamiento> result = tratamientoService.obtenerTratamientosDelUltimoMes();

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void cantidadTratamientosporMedicamentoUltimoMes() {
        List<Tratamiento> tratamientosEjemplo = new ArrayList<>();

        Medicamento medicamento1 = new Medicamento();
        medicamento1.setId(1L);
        medicamento1.setNombre("Medicamento 1");

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setId(2L);
        medicamento2.setNombre("Medicamento 2");

        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 9, 15));
        tratamiento1.setMedicamento(medicamento1);

        Tratamiento tratamiento2 = new Tratamiento();
        tratamiento2.setId(2L);
        tratamiento2.setDescripcion("Tratamiento 2");
        tratamiento2.setFecha(LocalDate.of(2023, 9, 20));
        tratamiento2.setMedicamento(medicamento2);

        tratamientosEjemplo.add(tratamiento1);
        tratamientosEjemplo.add(tratamiento2);

        when(tratamientoService.obtenerTratamientosDelUltimoMes()).thenReturn(tratamientosEjemplo);

        HashMap<String, Integer> result = tratamientoService.cantidadTratamientosporMedicamentoUltimoMes();

        Assertions.assertThat(result).containsKeys("Medicamento 1", "Medicamento 2");
    }

    @Test
    public void eliminarTratamiento() {
        Long id = 123L;

        doNothing().when(repositorioTratamientos).deleteById(id);

        tratamientoService.eliminarTratamiento(id);

        verify(repositorioTratamientos, times(1)).deleteById(id);
    }

    @Test
    public void existeTratamientoPorId() {
        Long idExistente = 123L;
        Long idNoExistente = 456L;

        when(repositorioTratamientos.existsById(idExistente)).thenReturn(true);
        when(repositorioTratamientos.existsById(idNoExistente)).thenReturn(false);

        boolean existeTratamientoExistente = tratamientoService.existeTratamientoPorId(idExistente);
        boolean existeTratamientoNoExistente = tratamientoService.existeTratamientoPorId(idNoExistente);

        assertTrue(existeTratamientoExistente);

        assertFalse(existeTratamientoNoExistente);

        verify(repositorioTratamientos, times(1)).existsById(idExistente);
        verify(repositorioTratamientos, times(1)).existsById(idNoExistente);
    }

    @Test
    public void ObtenerTratamientosPorVeterinario() {
        Long idVeterinario = 123L;

        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 10, 1));
        tratamiento1.setMascota(new Mascota());
        tratamiento1.setVeterinario(new Veterinario());
        tratamiento1.setMedicamento(new Medicamento());

        Tratamiento tratamiento2 = new Tratamiento();
        tratamiento2.setId(2L);
        tratamiento2.setDescripcion("Tratamiento 2");
        tratamiento2.setFecha(LocalDate.of(2023, 10, 2));
        tratamiento2.setMascota(new Mascota());
        tratamiento2.setVeterinario(new Veterinario());
        tratamiento2.setMedicamento(new Medicamento());

        List<Tratamiento> tratamientosEjemplo = new ArrayList<>();
        tratamientosEjemplo.add(tratamiento1);
        tratamientosEjemplo.add(tratamiento2);

        when(repositorioTratamientos.findTratamientosByVeterinarioId(idVeterinario)).thenReturn(tratamientosEjemplo);

        List<Tratamiento> result = tratamientoService.obtenerTratamientosPorVeterinario(idVeterinario);

        assertEquals(tratamientosEjemplo, result);

        verify(repositorioTratamientos, times(1)).findTratamientosByVeterinarioId(idVeterinario);
    }

    @Test
    public void testObtenerMascotasAtendidasPorVeterinario() {
      
        Tratamiento tratamiento = new Tratamiento();
        Mascota mascota = new Mascota();
        tratamiento.setMascota(mascota);

        when(repositorioTratamientos.findTratamientosByVeterinarioId(anyLong())).thenReturn(List.of(tratamiento));

        List<Mascota> result = tratamientoService.obtenerMascotasAtendidasPorVeterinario(1L);

        Assertions.assertThat(result).containsExactly(mascota);
    }

}
