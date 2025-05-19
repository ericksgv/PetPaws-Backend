package com.example.whiskervet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Usuario;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {
    
    @Autowired
    private RepositorioUsuarios repositorioUsuarios;


    @BeforeEach
    void init(){
        repositorioUsuarios.save(new Usuario( 12345L, "Erick", "erick@gmail.com", 1357924680L, "activo", new ArrayList<Mascota>()));
        repositorioUsuarios.save(new Usuario( 24680L, "Daniel", "daniel@gmail.com", 2468013579L, "activo", new ArrayList<Mascota>()));
        repositorioUsuarios.save(new Usuario( 13579L, "David", "david@gmail.com", 1256349078L, "activo", new ArrayList<Mascota>()));

    }

    @Test
    public void UsuarioRepositoryTest_save_Usuario(){
        // 1. Arrange
        Usuario usuario = new Usuario(6789012345L, "Juan", "juan@gmail.com", 4394309239L, "activo", new ArrayList<Mascota>());
        
        // 2. Act
        Usuario usuarioCreado = repositorioUsuarios.save(usuario);

        // 3. Assert
        Assertions.assertThat(usuarioCreado).isNotNull();
    }

    @Test
    public void UsuarioRepositoryTest_findAll_NotEmptyList(){
        // 1. Arrange
        Usuario usuario = new Usuario( 5792460824L, "Laura", "laura@gmail.com", 24834783443L, "activo", new ArrayList<Mascota>());
      
        // 2. Act
        repositorioUsuarios.save(usuario);
        List<Usuario> usuarios = repositorioUsuarios.findAll();

        // 3. Assert
        Assertions.assertThat(usuarios).isNotNull();
        Assertions.assertThat(usuarios.size()).isEqualTo(4);
        Assertions.assertThat(usuarios.size()).isGreaterThan(0);
    }

    @Test
    public void UsuarioRepositoryTest_deleteById_EmptyUuario(){
        // 1. Arrange
        Long index = 2L;

        //2. Act
        repositorioUsuarios.deleteById(index);

        // 3. Assert
        Assertions.assertThat(repositorioUsuarios.findById(index)).isEmpty();
    }

    @Test
    public void ARepositorioUsuariosTest_updateById_Usuario(){
        // 1. Arrange
        Long index = 1L;

        // 2. Act
        Usuario usuarioOptional = repositorioUsuarios.findById(index).orElse(null);
        Usuario usuario = usuarioOptional;
        usuario.setNombre("Erick Santiago");
        Usuario usuarioActualizado = repositorioUsuarios.save(usuario);

        // 3. Assert
        Assertions.assertThat(usuarioActualizado).isNotNull();
        Assertions.assertThat(usuarioActualizado.getNombre()).isEqualTo("Erick Santiago");
    }

    @Test
    public void RepositorioUsuariosTest_findByCedula(){
        // 1. Arrange
        Long cedula = 12345L;

        // 2. Act
        List<Usuario> usuarios = repositorioUsuarios.findByCedula(cedula);

        // 3. Assert
        Assertions.assertThat(usuarios).isNotNull();
        Assertions.assertThat(usuarios.size()).isEqualTo(1);
        Assertions.assertThat(usuarios.get(0).getCedula()).isEqualTo(cedula);
    }

    @Test
    public void RepositorioUsuariosTest_existsByCedula(){
        // 1. Arrange
        Long cedula = 12345L;

        // 2. Act
        boolean exists = repositorioUsuarios.existsByCedula(cedula);

        // 3. Assert
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void RepositorioUsuariosTest_findUByCedula(){
        // 1. Arrange
        Long cedula = 12345L;

        // 2. Act
        Optional<Usuario> usuarioOptional = repositorioUsuarios.findUByCedula(cedula);

        // 3. Assert
        Assertions.assertThat(usuarioOptional).isPresent();
        Assertions.assertThat(usuarioOptional.get().getCedula()).isEqualTo(cedula);
    }

    @Test
    public void RepositorioUsuariosTest_findByCedulaContaining(){
        // 1. Arrange
        String cedula = "123";

        // 2. Act
        List<Usuario> usuarios = repositorioUsuarios.findByCedulaContaining(cedula);

        // 3. Assert
        Assertions.assertThat(usuarios).isNotNull();
        Assertions.assertThat(usuarios.size()).isEqualTo(1);
        Assertions.assertThat(usuarios.get(0).getCedula().toString()).contains(cedula);
    }



    
}
