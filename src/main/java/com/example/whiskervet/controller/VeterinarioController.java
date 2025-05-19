package com.example.whiskervet.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.whiskervet.dto.LoginDTO;
import com.example.whiskervet.dto.VeterinarioDTO;
import com.example.whiskervet.dto.VeterinarioMapper;
import com.example.whiskervet.entity.UserEntity;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioUserEntity;
import com.example.whiskervet.repository.RepositorioVeterinarios;
import com.example.whiskervet.security.CustomUserDetailService;
import com.example.whiskervet.security.JWTGenerator;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicio;

import jakarta.transaction.Transactional;

@Transactional
@RestController
@RequestMapping("/veterinario")
@CrossOrigin(origins = "http://localhost:4200")
public class VeterinarioController {

    @Autowired
    VeterinarioServicio veterinarioService;

    @Autowired
    RepositorioUserEntity userRepository;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTGenerator jwtGenerator;

    


    @GetMapping("/details")
    public ResponseEntity<VeterinarioDTO> buscarUsuarioVeterinario() {
        String cedula = SecurityContextHolder.getContext().getAuthentication().getName();
        Long cedulaLong = Long.parseLong(cedula);

        Veterinario veterinario = veterinarioService.obtenerVeterinarioPorCedula(cedulaLong);
        VeterinarioDTO veterinarioDTO = VeterinarioMapper.INSTANCE.convert(veterinario);
        if(veterinario == null){
            return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.OK);
    }


    @GetMapping("/verificar-permisos/add")
    public ResponseEntity<String> verificarPermisos() {
        return new ResponseEntity<String>("veterinarioDTO", HttpStatus.OK);
    }


    @GetMapping("/find/{cedula}")
    public ResponseEntity<VeterinarioDTO> mostrarInfoVeterinario(@PathVariable("cedula") Long cedula) {
        Veterinario veterinario = veterinarioService.obtenerVeterinarioPorCedula(cedula);
        VeterinarioDTO veterinarioDTO = VeterinarioMapper.INSTANCE.convert(veterinario);
        if (veterinario == null) {
            return new ResponseEntity<>(null,  HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<VeterinarioDTO>> mostrarVeterinarios() {
        List<Veterinario> lista = veterinarioService.obtenerTodosLosVeterinarios();
        List <VeterinarioDTO> listaDTO = new ArrayList<>();
        for (Veterinario v : lista){
            listaDTO.add(VeterinarioMapper.INSTANCE.convert(v));
        }
        ResponseEntity<List<VeterinarioDTO>> response = new ResponseEntity<>(listaDTO, HttpStatus.OK);
        return response;
    }

    @PostMapping("/add")
    public ResponseEntity agregarVeterinario(@RequestBody Veterinario veterinario){
        if(userRepository.existsByUsername(veterinario.getCedula().toString())){
            return new ResponseEntity<String>("El veterinario ya está registrado", HttpStatus.BAD_REQUEST);
        }
        UserEntity userEntity = customUserDetailService.saveVeterinario(veterinario);
        veterinario.setUser(userEntity);

         Veterinario newVeterinario = veterinarioService.guardarVeterinario(veterinario);
         VeterinarioDTO veterinarioDTO = VeterinarioMapper.INSTANCE.convert(newVeterinario);
            if(newVeterinario == null){
                return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO infoLogin){
        /*
        Veterinario veterinario = veterinarioService.obtenerVeterinarioPorCedula(infoLogin.getCedula());
        VeterinarioDTO veterinarioDTO = VeterinarioMapper.INSTANCE.convert(veterinario);
        //|| !veterinario.checkPassword(infoLogin.getContrasena())
        if(veterinario == null ){
            return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<VeterinarioDTO>(veterinarioDTO, HttpStatus.CREATED);
         */


        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(infoLogin.getCedula().toString(), infoLogin.getContrasena()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
public ResponseEntity logout(@RequestHeader("Authorization") String token) {
    // Extraer el token del encabezado de autorización (debe venir en el formato "Bearer <token>")
    String authToken = token.substring(7);

    // Revocar el token (invalidar al hacer logout)
    jwtGenerator.revokeToken(authToken);

    return new ResponseEntity<>("Logout exitoso", HttpStatus.OK);
}


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> eliminarVeterinario(@PathVariable("id") Long id){
        List<Veterinario> veterinarios = veterinarioService.obtenerTodosLosVeterinarios();
        for (Veterinario veterinario : veterinarios) {
            if (veterinario.getId().equals(id)) {
                veterinario.setEstado("inactivo");
                veterinarioService.guardarVeterinario(veterinario);
                break;
            }
        }
        return new ResponseEntity<>("DELETED",HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{cedula}")
    public ResponseEntity<VeterinarioDTO> actualizarVeterinario(@PathVariable("cedula") Long cedula, @RequestBody Veterinario veterinario) {
        Veterinario vetFind = veterinarioService.obtenerVeterinarioPorCedula(cedula);
        veterinario.setId(vetFind.getId());
        Veterinario vetUpdated = veterinarioService.guardarVeterinario(veterinario);
        VeterinarioDTO veterinarioDTO = VeterinarioMapper.INSTANCE.convert(vetUpdated);
        return new ResponseEntity<>(veterinarioDTO,HttpStatus.OK);
    }

    @PutMapping("/activate/{cedula}")
    public ResponseEntity<String> activarVeterinario(@PathVariable("cedula") Long cedula) {
        veterinarioService.activarVeterinario(cedula);
        return new ResponseEntity<>("Veterinario activado", HttpStatus.OK);
    }
    
    @GetMapping("/filtrar/{cedula}")
    public ResponseEntity<List<VeterinarioDTO>> filtrarVeterinariosPorCedula(@PathVariable("cedula") Long cedula) {
        List<Veterinario> veterinarios = veterinarioService.filtrarVeterinariosCedula(cedula);
        List <VeterinarioDTO> listaDTO = new ArrayList<>();
        for (Veterinario v : veterinarios){
            listaDTO.add(VeterinarioMapper.INSTANCE.convert(v));
        }
        if (veterinarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listaDTO, HttpStatus.OK);
        }
    }

        @GetMapping("/filtrarNombre/{nombre}")
    public ResponseEntity<List<VeterinarioDTO>> filtrarVeterinariosPorNombre(@PathVariable("nombre") String nombre) {
        List<Veterinario> veterinarios = veterinarioService.filtrarVeterinariosNombre(nombre);
        List <VeterinarioDTO> listaDTO = new ArrayList<>();
        for (Veterinario v : veterinarios){
            listaDTO.add(VeterinarioMapper.INSTANCE.convert(v));
        }
        if (veterinarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listaDTO, HttpStatus.OK);
        }
    }
    

}
