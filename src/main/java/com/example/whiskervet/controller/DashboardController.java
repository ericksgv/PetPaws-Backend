package com.example.whiskervet.controller;

import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.dto.UsuarioDTO;
import com.example.whiskervet.dto.UsuarioMapper;
import com.example.whiskervet.entity.Administrador;
import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.repository.RepositorioAdministrador;
import com.example.whiskervet.servicio.mascota.MascotaServiceImpl;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;
import com.example.whiskervet.servicio.tratamiento.TratamientoServiceImpl;
import com.example.whiskervet.servicio.usuario.UsuarioServiceImpl;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicioImpl;
import com.github.javafaker.Bool;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {
    
    @Autowired
    private MascotaServiceImpl mascotaService;

    @Autowired
    private VeterinarioServicioImpl veterinarioService;

    @Autowired
    private MedicamentoServiceImpl medicamentoService;

    @Autowired 
    private TratamientoServiceImpl tratamientoService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private RepositorioAdministrador repositorioAdministrador;

    @GetMapping("/details")
    public ResponseEntity<Boolean> buscarUsuarioAdmin() {
        String cedula = SecurityContextHolder.getContext().getAuthentication().getName();
        Long cedulaLong = Long.parseLong(cedula);
        System.out.println("cedula" + cedula);

        Administrador usuario = repositorioAdministrador.findByCedula(cedulaLong);
        System.out.println("cedula" + usuario);
        if(usuario == null){
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @GetMapping("/veterinarios/activos")
    public Long mostrarVeterinariosActivos() {
        System.out.println("Veterinarios activos: " + veterinarioService.obtenerVeterinariosActivos());
        return veterinarioService.obtenerVeterinariosActivos();
    }

    @GetMapping("/veterinarios/inactivos")
    public Long mostrarVeterinariosinActivos() {
        System.out.println("Veterinarios inactivos: " + veterinarioService.obtenerVeterinariosInactivos());
        return veterinarioService.obtenerVeterinariosInactivos();
    }

    @GetMapping ("/mascotas/total")
    public Long mostrarTotalMascotas(){
        System.out.println("Total mascotas: " + mascotaService.obtenerTotalMascotas());
        return mascotaService.obtenerTotalMascotas();
    }

    @GetMapping ("/mascotas/tratamiento")
    public Long mostrarMascotasTratamiento(){
        System.out.println("Total mascotas en tratamiento: " + mascotaService.obtenerMascotasTratamiento());
        return mascotaService.obtenerMascotasTratamiento();
    }

    @GetMapping("/medicamentos/ventas-totales")
    public float obtenerVentasTotales() {
        System.out.println("Ventas totales: " + medicamentoService.obtenerVentasTotales());
        return medicamentoService.obtenerVentasTotales();
    }

    @GetMapping("/medicamentos/ganancias-totales")
    public float obtenerGananciasTotales() {
        System.out.println("Ganancias totales: " + medicamentoService.obtenerGananciasTotales());
        return medicamentoService.obtenerGananciasTotales();
    }

    @GetMapping("medicamentos/top3-mas-vendidos")
    public List<Medicamento> obtenerTop3MasVendidos() {
        System.out.println("Top 3 mas vendidos: " + medicamentoService.obtenerTop3MasVendidos());
        return medicamentoService.obtenerTop3MasVendidos();
    }


    @GetMapping("tratamientos/ultimo-mes")
        public List<Tratamiento> obtenerTratamientosDelUltimoMes() {
        List<Tratamiento> tratamientos = tratamientoService.obtenerTratamientosDelUltimoMes();
        return tratamientos;
    }


    @GetMapping("medicamentos/tratamientos/ultimo-mes")
    public HashMap<String, Integer> cantidadTratamientosporMedicamentoUltimoMes() {
        return tratamientoService.cantidadTratamientosporMedicamentoUltimoMes();
    }

    @GetMapping("usuarios/total")
    public Long obtenerTotalUsuarios() {
        return usuarioService.obtenerTotalUsuarios();
    }
    
}
