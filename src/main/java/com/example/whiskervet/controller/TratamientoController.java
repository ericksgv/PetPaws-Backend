package com.example.whiskervet.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.servicio.correoelectronico.EmailService;
import com.example.whiskervet.servicio.mascota.MascotaServiceImpl;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;
import com.example.whiskervet.servicio.tratamiento.TratamientoServiceImpl;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicioImpl;

@RestController
@RequestMapping("/tratamiento")
@CrossOrigin(origins = "http://localhost:4200")
public class TratamientoController {

    @Autowired
    private TratamientoServiceImpl tratamientoService;

    @Autowired
    private MascotaServiceImpl mascotaService;

    @Autowired
    private MedicamentoServiceImpl medicamentoService;

    @Autowired
    private VeterinarioServicioImpl veterinarioService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/find/{id}")
    public ResponseEntity<Tratamiento> mostrarTratamiento(@PathVariable Long id) {

        Tratamiento tratamientoBuscado = tratamientoService.obtenerTratamientoPorId(id).orElse(null);

        if (tratamientoBuscado == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(tratamientoBuscado, HttpStatus.OK);
    }


    @GetMapping("/verificar-permisos/add")
    public ResponseEntity<String> verificarPermisos() {
        return new ResponseEntity<String>("tratamientoDTO", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tratamiento>> mostrarTratamientos() {
        List<Tratamiento> allTratamientos = tratamientoService.obtenerTodosLosTratamientos();
        return new ResponseEntity<>(allTratamientos, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminarTratamiento(@PathVariable Long id) {
        tratamientoService.eliminarTratamiento(id);
        return new ResponseEntity<>("Tratamiento Borrado.", HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Tratamiento> actualizarTratamiento(@PathVariable Long id, Tratamiento tratamiento) {
        Tratamiento tratamientoActualizado = tratamientoService.guardarTratamiento(tratamiento);

        return new ResponseEntity<>(tratamientoActualizado, HttpStatus.OK);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Tratamiento> agregarTratamiento(@RequestBody Map<String, String> tratamientoData) {
        Tratamiento tratamiento = new Tratamiento();

        System.out.println("TRATAMIENTO: " + tratamiento);

        long idMascota = Long.parseLong(tratamientoData.get("mascotaId"));
        long idVeterinario = Long.parseLong(tratamientoData.get("veterinarioId"));
        long idMedicamento = Long.parseLong(tratamientoData.get("medicamentoId"));
        LocalDate fecha = LocalDate.parse(tratamientoData.get("fecha"));

        tratamiento.setFecha(fecha);
        tratamiento.setDescripcion(tratamientoData.get("descripcion"));
        tratamiento.setMascota(mascotaService.obtenerMascotaPorId(idMascota));
        tratamiento.setVeterinario(veterinarioService.obtenerVeterinarioPorCedula(idVeterinario));
        tratamiento.setMedicamento(medicamentoService.obtenerMedicamentoPorId(idMedicamento).orElse(null));

        Tratamiento tratamientoCreado = tratamientoService.guardarTratamiento(tratamiento);

        if (tratamientoCreado == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        emailService.enviarCorreo(tratamientoCreado);
        return new ResponseEntity<>(tratamientoCreado, HttpStatus.CREATED);
    }

    @GetMapping("/veterinario/{id}")
    public ResponseEntity<List<Tratamiento>> mostrarTratamientosVeterinario(@PathVariable Long id) {
        List<Tratamiento> tratamientosVeterinario = tratamientoService.obtenerTratamientosPorVeterinario(id);
        return new ResponseEntity<>(tratamientosVeterinario, HttpStatus.OK);
    }

    @GetMapping("/mascotasAtendidasPorVeterinario/{id}")
    public ResponseEntity<List<Mascota>> obtenerMascotasAtendidasPorVeterinario(@PathVariable Long id) {
        System.out.println(tratamientoService.obtenerMascotasAtendidasPorVeterinario(id));
        List<Mascota> mascotasAtendidas = tratamientoService.obtenerMascotasAtendidasPorVeterinario(id);

        return new ResponseEntity<>(mascotasAtendidas, HttpStatus.OK);
    }

}

/*
    Códigos de respuesta:
    - Http create y Bad request: Para la creación exitosa o fallida de algo en una base de datos.

 */