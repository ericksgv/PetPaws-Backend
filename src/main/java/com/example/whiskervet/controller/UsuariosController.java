package com.example.whiskervet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.whiskervet.controller.errorcontroller.CustomException;
import com.example.whiskervet.dto.LoginDTO;
import com.example.whiskervet.dto.UsuarioDTO;
import com.example.whiskervet.dto.UsuarioMapper;
import com.example.whiskervet.dto.VeterinarioDTO;
import com.example.whiskervet.dto.VeterinarioMapper;
import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.UserEntity;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioUserEntity;
import com.example.whiskervet.security.CustomUserDetailService;
import com.example.whiskervet.security.JWTGenerator;
import com.example.whiskervet.servicio.usuario.UsuarioService;


@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuariosController {

    @Autowired
    private UsuarioService usuarioService;

        @Autowired
    RepositorioUserEntity clienteRepository;

    @Autowired
    RepositorioUserEntity userRepository;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTGenerator jwtGenerator;

        @GetMapping("/details")
    public ResponseEntity<UsuarioDTO> buscarUsuarioCliente() {
        String cedula = SecurityContextHolder.getContext().getAuthentication().getName();
        Long cedulaLong = Long.parseLong(cedula);

        Usuario usuario = usuarioService.obtenerUsuarioPorCedula(cedulaLong).get();
        UsuarioDTO usuarioDTO = UsuarioMapper.INSTANCE.convert(usuario);
        if(usuario == null){
            return new ResponseEntity<UsuarioDTO>(usuarioDTO, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UsuarioDTO>(usuarioDTO, HttpStatus.OK);
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
    
    @GetMapping("/find/{cedula}")
    public UsuarioDTO mostrarInfoUsuario(@PathVariable("cedula") Long cedula) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorCedula(cedula);
        Usuario usuario = usuarioOptional.get();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        if (usuarioOptional.isPresent()) {
           usuarioDTO = UsuarioMapper.INSTANCE.convert(usuario);

        } else {
            // Manejar el caso en el que no se encuentre un usuario con la cédula
            throw new CustomException("UsuarioNotFound", cedula);
        }
        return usuarioDTO;
    }

    @GetMapping("/all")
    public List<UsuarioDTO> mostrarUsuarios() {
        List<Usuario> lista = usuarioService.obtenerTodosLosUsuarios();
        List <UsuarioDTO> listaDTO = new ArrayList<>();
        for (Usuario u : lista){
            listaDTO.add(UsuarioMapper.INSTANCE.convert(u));
        }
        return listaDTO;
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarUsuario(@PathVariable("id") Long id) {
        usuarioService.eliminarUsuario(id);
    }

    @PutMapping("/update/{cedula}")
    public void actualizarUsuario(@PathVariable("cedula") Long cedula, @RequestBody Usuario usuario) {
        usuarioService.guardarUsuario(usuario);
    }

    @PutMapping("/activate/{id}")
    public void activarUsuario(@PathVariable("id") Long id) {
        usuarioService.activarUsuario(id);
    }

    @PostMapping("/agregar")
    public boolean agregarUsuarioConMascota(@RequestBody Usuario usuario) {
        if(clienteRepository.existsByUsername(usuario.getCedula().toString())){
            return false;
        }

        UserEntity userEntity = customUserDetailService.saveCliente(usuario);
        Usuario u = new Usuario();
        u.setCedula(usuario.getCedula());
        u.setNombre(usuario.getNombre());
        u.setCorreo(usuario.getCorreo());
        u.setCelular(usuario.getCelular());
        u.setEstado(usuario.getEstado());
        u.setMascotas(usuario.getMascotas());
        u.setUser(userEntity);
        Usuario newUsuario = usuarioService.guardarUsuario(u);
        if ( newUsuario == null)
            return false;
        return true;
    }

    @GetMapping("/buscarPorCedula")
    @ResponseBody
    public ResponseEntity<List<UsuarioDTO>> buscarUsuariosPorCedula(@RequestParam Long cedula) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorCedula(cedula);
        List <UsuarioDTO> listaDTO = new ArrayList<>();
        for (Usuario u : usuarios){
            listaDTO.add(UsuarioMapper.INSTANCE.convert(u));
        }
        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(listaDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/mascotas/{cedula}")
    public List<Mascota> mostrarMascotasDeUsuario(@PathVariable("cedula") Long cedula) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorCedula(cedula);
        Usuario usuario = usuarioOptional.get();
        return usuario.getMascotas();
    }

    @PostMapping("/logout")
public ResponseEntity logout(@RequestHeader("Authorization") String token) {
    // Extraer el token del encabezado de autorización (debe venir en el formato "Bearer <token>")
    String authToken = token.substring(7);

    // Revocar el token (invalidar al hacer logout)
    jwtGenerator.revokeToken(authToken);

    return new ResponseEntity<>("Logout exitoso", HttpStatus.OK);
}

@GetMapping("/verificar-permisos/add")
public ResponseEntity<String> verificarPermisos() {
    return new ResponseEntity<String>("usuarioDTO", HttpStatus.OK);
}

    @GetMapping("/filtrar/{cedula}")
    public List<UsuarioDTO> filtrarUsuariosPorCedula(@PathVariable("cedula") Long cedula) {
        //System.out.println(usuarioService.filtrarUsuariosPorCedula(cedula));
        List<Usuario> lista = usuarioService.filtrarUsuariosPorCedula(cedula);
        List <UsuarioDTO> listaDTO = new ArrayList<>();
        for (Usuario u : lista){
            listaDTO.add(UsuarioMapper.INSTANCE.convert(u));
        }
        return listaDTO;
    }
}
