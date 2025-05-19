package com.example.whiskervet.controller;

import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.whiskervet.dto.CitasDTO;
import com.example.whiskervet.entity.Citas;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.repository.RepositorioUsuarios;
import com.example.whiskervet.servicio.cita.CitaService;
import com.example.whiskervet.servicio.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/citas")
@CrossOrigin(origins = "http://localhost:4200")
public class CitasController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private RepositorioUsuarios repositorioUsuarios;

    @GetMapping("/find/{id}")
    public Citas mostrarInfoCita(@PathVariable("id") Long id) {
        Citas cita = citaService.getCitaById(id);
        return cita;
    }

    @GetMapping("/all")
    public List<Citas> mostrarCitas() {
        return citaService.getAllCitas();
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarCita(@PathVariable("id") Long id) {
        citaService.deleteCita(id);
    }

    @PutMapping("/update/{id}")
    public void actualizarCita(@PathVariable("id") Long id, @RequestBody Citas cita) {
        citaService.saveCita(cita);
    }

    @PostMapping("/agregar")
    public void agregarCita(@RequestBody Citas cita) {
        citaService.saveCita(cita);
    }

   @GetMapping("/disponibles/{fecha}")
    @Operation(summary = "Obtener horas disponibles para un día específico")
    public List<Date> getHorasDisponiblesParaDia(
            @Parameter(description = "Fecha para la que se desean obtener las horas disponibles",
                    schema = @Schema(type = "string", format = "date"))
            @PathVariable("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha) {
        return citaService.getHorasDisponiblesParaDia(fecha);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CitasDTO> getCitasByCliente(@PathVariable Long usuarioId) {
        return citaService.buscarCitasPorIdCliente(usuarioId);
    }
    
    @GetMapping("/today")
    public List<CitasDTO> getCitasDelDiaActual() {
        return citaService.getCitasDelDiaActual();
    }
}
