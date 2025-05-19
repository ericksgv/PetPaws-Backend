package com.example.whiskervet.security;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.whiskervet.entity.Administrador;
import com.example.whiskervet.entity.Role;
import com.example.whiskervet.entity.UserEntity;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioUserEntity;
import com.example.whiskervet.repository.RoleRepository;

@Service
public class CustomUserDetailService implements UserDetailsService{

    @Autowired
    private RepositorioUserEntity userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*Unico método para traer la informacion de un usuario a traves de su username */
    /*Retorna un USerDetails, que es la entidad básica en springboot que unicamente tiene
     * username, password y authorities
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Buscar al usuario si no se encuentra traer una excepcion
        //El usuario que se carga es de tipo USer Entity, el que nosotros creamos
       UserEntity userDB = userRepository.findByUsername(username).orElseThrow(
           () -> new UsernameNotFoundException("User not found")
       );
       UserDetails userDetails = new User(userDB.getUsername(),
        userDB.getPassword(),
         mapRolesToAuthorities(userDB.getRoles()));

        //El usuario que se retorna es de tipo UserDetail
        //Se mapean los datos desde el UserEntity a UserDetail
        //Es necesario pasar como tercer parametro grantedAutathorities
       return userDetails;

    }

        //Pasar de roles a GrantedAuthoritys
        private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
            return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        }
    
        public UserEntity saveCliente(Usuario usuario){
            UserEntity user = new UserEntity();
            user.setUsername(usuario.getCedula().toString());
            user.setPassword(passwordEncoder.encode("1"));
            Role roles = roleRepository.findByName("CLIENTE").get();
            user.setRoles(List.of(roles));
            userRepository.save(user);
            return user;
        }

        public UserEntity saveVeterinario(Veterinario veterinario){
            UserEntity user = new UserEntity();
            user.setUsername(veterinario.getCedula().toString());
            user.setPassword(passwordEncoder.encode(veterinario.getPasswordHash()));
            Role roles = roleRepository.findByName("VETERINARIO").get();
            user.setRoles(List.of(roles));
            userRepository.save(user);
            return user;
        }
        
        public UserEntity saveAdmin(Administrador admin){
            UserEntity user = new UserEntity();
            user.setUsername(admin.getCedula().toString());
            user.setPassword(passwordEncoder.encode(admin.getPassword()));
            Role roles = roleRepository.findByName("ADMIN").get();
            user.setRoles(List.of(roles));
            userRepository.save(user);
            return user;
        }
              
}
