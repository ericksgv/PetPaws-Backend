package com.example.whiskervet.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.example.whiskervet.dto.TratamientoDTO;
import com.example.whiskervet.dto.UsuarioJSON;
import com.example.whiskervet.entity.Administrador;
import com.example.whiskervet.entity.Mascota;
import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.entity.Role;
import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.entity.UserEntity;
import com.example.whiskervet.entity.Usuario;
import com.example.whiskervet.entity.Veterinario;
import com.example.whiskervet.repository.RepositorioAdministrador;
import com.example.whiskervet.repository.RepositorioMascotas;
import com.example.whiskervet.repository.RepositorioMedicamentos;
import com.example.whiskervet.repository.RepositorioTratamientos;
import com.example.whiskervet.repository.RepositorioUserEntity;
import com.example.whiskervet.repository.RepositorioUsuarios;
import com.example.whiskervet.repository.RepositorioVeterinarios;
import com.example.whiskervet.repository.RoleRepository;
import com.example.whiskervet.servicio.mascota.MascotaServiceImpl;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;
import com.example.whiskervet.servicio.tratamiento.TratamientoServiceImpl;
import com.example.whiskervet.servicio.usuario.UsuarioService;
import com.example.whiskervet.servicio.veterinario.VeterinarioServicioImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Component
@Transactional
public class DataBaseInit implements ApplicationRunner {

    @Autowired
    RepositorioUsuarios repositorioUsuarios;

    @Autowired
    RepositorioMascotas repositorioMascotas;

    @Autowired
    RepositorioAdministrador repositorioAdministrador;

    @Autowired
    RepositorioVeterinarios repositorioVeterinarios;

    @Autowired
    RepositorioMedicamentos repositorioMedicamentos;

    @Autowired
    RepositorioTratamientos repositorioTratamientos;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private VeterinarioServicioImpl veterinarioService;

    @Autowired
    private MedicamentoServiceImpl medicamentoService;

    @Autowired
    private MascotaServiceImpl mascotaService;

    @Autowired
    private TratamientoServiceImpl tratamientoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RepositorioUserEntity userRepository;

        @Override
    public void run(ApplicationArguments args) throws Exception {
        crearRoles();
        cargarAdministrador();
        cargarUsuarios();
        cargarVeterinarios();
        cargarMedicamentos();
        cargarTratamientos();
    }

    private void crearRoles(){
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("VETERINARIO"));
        roleRepository.save(new Role("CLIENTE"));
    }

    private String loadJsonFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        InputStream inputStream = resource.getInputStream();
        byte[] jsonBytes = FileCopyUtils.copyToByteArray(inputStream);
        return new String(jsonBytes, StandardCharsets.UTF_8);
    }

    private void cargarAdministrador() {
        Administrador admin = new Administrador();
        admin.setCedula(123456789L); // Aquí establece la cédula del administrador
        admin.setPassword("12345");
        UserEntity userEntity = guardarUsuarioAdmin(admin);
        admin.setUser(userEntity);
        repositorioAdministrador.save(admin);
    }

    private void cargarUsuarios() throws IOException {
        UserEntity userEntity;
        // Cargar el contenido del archivo JSON
        String jsonFileName = "data.json"; // Nombre del archivo JSON en resources
        String jsonContent = loadJsonFile(jsonFileName);

        // Convertir el JSON en objetos Java
        ObjectMapper objectMapper = new ObjectMapper();
        List<UsuarioJSON> usuarios = objectMapper.readValue(jsonContent, new TypeReference<List<UsuarioJSON>>() {
        });

        // Iterar a través de los usuarios y sus mascotas
        for (UsuarioJSON usuario : usuarios) {
            // Guardar el usuario en la base de datos utilizando el servicio
            Usuario usuarioTemp = Usuario.builder()
            .cedula(usuario.getCedula())
            .nombre(usuario.getNombre())
            .correo(usuario.getCorreo())
            .celular(usuario.getCelular())
            .estado("activo")
            .mascotas(usuario.getMascotas())
            .build();
            userEntity = guardarUsuarioUsuario(usuarioTemp);
            usuarioTemp.setUser(userEntity);
            Usuario savedUsuario = usuarioService.guardarUsuario(usuarioTemp);
            

            // Obtener las mascotas del usuario
            List<Mascota> mascotas = usuario.getMascotas();
            for (Mascota mascota : mascotas) {
                // Establecer la relación entre el usuario y sus mascotas
                mascota.setDuenio(savedUsuario);
                mascota.setEstado("activo");
            }

            // Guardar las mascotas en la base de datos utilizando el repositorio de
            // mascotas
            repositorioMascotas.saveAll(mascotas);
        }
    }

    private void cargarVeterinarios() throws IOException {
        UserEntity userEntity;

        // Cargar el contenido del archivo JSON
        String jsonFileName = "data_vet.json";
        String jsonContent = loadJsonFile(jsonFileName);
        ObjectMapper objectMapper = new ObjectMapper();
        // Convertir el JSON en objetos Java
        List<Veterinario> veterinarios = objectMapper.readValue(jsonContent, new TypeReference<List<Veterinario>>() {
        });

        for (Veterinario v : veterinarios) {
            // Guardar el usuario en la base de datos utilizando el servicio
            userEntity = guardarUsuarioVeterinario(v);
            v.setUser(userEntity);
            veterinarioService.guardarVeterinario(v);
        }
    }

    private void cargarMedicamentos() {
        try {
            InputStream excelFile = getClass().getClassLoader().getResourceAsStream("MEDICAMENTOS_VETERINARIA.xlsx");
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheet("MEDICAMENTOS BD FINAL");

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Medicamento medicamento = new Medicamento();
                    medicamento.setNombre(row.getCell(0).getStringCellValue());
                    medicamento.setPrecioVenta((float) row.getCell(1).getNumericCellValue());
                    medicamento.setPrecioCompra((float) row.getCell(2).getNumericCellValue());
                    medicamento.setUnidadesDisponibles((int) row.getCell(3).getNumericCellValue());
                    medicamento.setUnidadesVendidas((int) row.getCell(4).getNumericCellValue());
                    medicamentoService.guardarMedicamento(medicamento);
                }
            }

            if (workbook != null) {
                workbook.close();
            }
            excelFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Transactional
    private void cargarTratamientos() throws IOException{
        
        String jsonFileName = "data_tratamientos.json"; // Nombre del archivo JSON en resources
        String jsonContent = loadJsonFile(jsonFileName);

        // Convertir el JSON en objetos Java
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<TratamientoDTO> tratamientos = objectMapper.readValue(jsonContent, new TypeReference<List<TratamientoDTO>>() {
        });

        // Iterar a través de los usuarios y sus mascotas
        for (TratamientoDTO tratamiento : tratamientos) {
            // Guardar el tratamiento en la base de datos
            Tratamiento tratamientoTemp = new Tratamiento();
            tratamientoTemp.setDescripcion(tratamiento.getDescripcion());
            tratamientoTemp.setFecha(LocalDate.parse(tratamiento.getFecha().toString()));

            tratamientoTemp.setMascota(mascotaService.obtenerMascotaPorId(tratamiento.getIdMascota()));
            tratamientoTemp.setVeterinario(veterinarioService.obtenerVeterinarioPorCedula(tratamiento.getCedulaVeterinario()));
            tratamientoTemp.setMedicamento(medicamentoService.obtenerMedicamentoPorId(tratamiento.getIdMedicamento()).orElse(null));

            tratamientoService.guardarTratamiento(tratamientoTemp);
            //emailService.enviarCorreo(tratamientoTemp);




        }
    }

    private UserEntity guardarUsuarioAdmin(Administrador admin){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(admin.getCedula().toString());
        userEntity.setPassword(passwordEncoder.encode(admin.getPassword()));
        Role roles = roleRepository.findByName("ADMIN").get();
        userEntity.setRoles(List.of(roles));
        return userRepository.save(userEntity);
    }


    private UserEntity guardarUsuarioVeterinario(Veterinario veterinario){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(veterinario.getCedula().toString());
        userEntity.setPassword(passwordEncoder.encode(veterinario.getPasswordHash()));
        Role roles = roleRepository.findByName("VETERINARIO").get();
        userEntity.setRoles(List.of(roles));
        return userRepository.save(userEntity);
    }


    private UserEntity guardarUsuarioUsuario(Usuario usuario){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(usuario.getCedula().toString());
        userEntity.setPassword(passwordEncoder.encode("1"));
        Role roles = roleRepository.findByName("CLIENTE").get();
        userEntity.setRoles(List.of(roles));
        return userRepository.save(userEntity);
    }
}