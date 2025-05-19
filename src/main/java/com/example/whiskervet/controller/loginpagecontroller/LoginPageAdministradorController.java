package com.example.whiskervet.controller.loginpagecontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.dto.LoginDTO;
import com.example.whiskervet.entity.Administrador;
import com.example.whiskervet.repository.RepositorioAdministrador;
import com.example.whiskervet.repository.RepositorioUserEntity;
import com.example.whiskervet.security.CustomUserDetailService;
import com.example.whiskervet.security.JWTGenerator;

@RestController
@RequestMapping("/loginAdministrador")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginPageAdministradorController {

        @Autowired
    RepositorioUserEntity userRepository;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTGenerator jwtGenerator;

    @Autowired
    RepositorioAdministrador repositorioAdministrador;

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
        System.out.println("TOKEN: " + token);

        return new ResponseEntity<String>(token, HttpStatus.OK);
    }
}
