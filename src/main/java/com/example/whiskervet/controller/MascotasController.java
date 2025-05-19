package com.example.whiskervet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.dto.TratamientoInfoDTO;
import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.servicio.mascota.MascotaServiceImpl;
import com.example.whiskervet.servicio.usuario.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/mascota")
@CrossOrigin(origins = "http://localhost:4200")
public class MascotasController {

    @Autowired
    private MascotaServiceImpl mascotaService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/find/{id}")
    public Mascota mostrarInfoMascota(@PathVariable("id") Long id) {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id);
        return mascota;
    }

    @GetMapping("/all")
    @Operation (summary = "Obtener todas las mascotas")
    public List<Mascota> mostrarMascotas() {
        return mascotaService.obtenerTodasLasMascotas();
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarMascota(@PathVariable("id") Long id) {
        mascotaService.eliminarMascotaPorId(id);
    }

    @PutMapping("/update/{id}")
    public void actualizarMascota(@PathVariable("id") Long id, @RequestBody Mascota mascota) {
        mascotaService.guardarMascota(mascota);
    }

    @PostMapping("/agregar")
    public void agregarMascota(@RequestBody Mascota mascota) {
        mascotaService.guardarMascota(mascota);
    }

        @PostMapping("/agregarConCedula/{cedula}")
    public void agregarMascotaCedula(@PathVariable("cedula") Long cedula, @RequestBody Mascota mascota) {
        Usuario duenio = usuarioService.obtenerUsuarioPorCedula(cedula).orElse(null); 
        mascota.setDuenio(duenio);
        mascotaService.guardarMascota(mascota);
    }

    @PutMapping("/activate/{id}")
    public void activarVeterinario(@PathVariable("id") Long id) {
        mascotaService.activarMascota(id);
    }

    @GetMapping("/addWithVeterinarioAndCedula")
    public String mostrarFormularioAgregarConVeterinarioYCedula(
            @RequestParam("cedula") Long cedulaUsuario,
            Model model) {
        Mascota mascota = new Mascota(null, null, 0, cedulaUsuario, null, null, null);
        model.addAttribute("mascota", mascota);
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        model.addAttribute("cedulausuario", cedulaUsuario);
        return "mascotas/crear_mascota";
    }

        @GetMapping("/filtrar/{nombre}")
    public List<Mascota> filtrarMascotasPorCedula(@PathVariable("nombre") String nombre) {
        System.out.println(mascotaService.filtrarMascotasPorNombre(nombre));
        return mascotaService.filtrarMascotasPorNombre(nombre);
    }

        @GetMapping("/verificar-permisos/add")
    public ResponseEntity<String> verificarPermisos() {
        return new ResponseEntity<String>("mascotaDTO", HttpStatus.OK);
    }

       @GetMapping("/tratamientos/{id}")
    public List<TratamientoInfoDTO> buscarTratamientosMascota(@PathVariable("id") Long id) {
       List<Tratamiento> tratamientos = mascotaService.obtenerMascotaPorId(id).getTratamientos();
       List<TratamientoInfoDTO> tratamientosDTO = new ArrayList<>();
       for(Tratamiento t: tratamientos){
        TratamientoInfoDTO tratamientoDTO = TratamientoInfoDTO.builder()
        .descripcion(t.getDescripcion())
        .nombreMedicamento(t.getMedicamento().getNombre())
        .nombreVeterinario(t.getVeterinario().getNombre())
        .fecha(t.getFecha())
        .build();
    
        tratamientosDTO.add(tratamientoDTO);
       }

       return tratamientosDTO;

    }
}