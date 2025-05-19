package com.example.whiskervet.controller;

import com.example.whiskervet.entity.Tratamiento;
import com.example.whiskervet.servicio.tratamiento.TratamientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = TratamientoController.class)
@ActiveProfiles ("test")
@RunWith(SpringRunner.class)
public class TratamientoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TratamientoService tratamientoService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void TratamientoController_agregarTratamiento_Tratamiento() throws Exception{
        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 10, 1));

        when(tratamientoService.guardarTratamiento(Mockito.any(Tratamiento.class))).thenReturn(tratamiento1);

        ResultActions response =  mockMvc.perform(
                post("/tratamiento/agregar")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tratamiento1))
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Tratamiento 1"))
                .andExpect(jsonPath("$.fecha").value("2023-10-01")
                );


    }

    @Test
    public void TratamientoController_mostrarTratamientos_TramientosList() throws Exception {

        when(tratamientoService.obtenerTodosLosTratamientos()).thenReturn(
                List.of(
                        new Tratamiento(1L, "Tratamiento 1", LocalDate.of(2023, 10, 1), null, null, null),
                        new Tratamiento(2L, "Tratamiento 2", LocalDate.of(2023, 10, 5), null, null, null)
                )
        );

        ResultActions response = mockMvc.perform(
                get("/tratamiento/all")
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2));


    }

    @Test
    public void TratamientoController_mostrarTratamiento1_Tratamiento_Encontrado() throws Exception {

        // Arrange
        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 10, 1));

        when(tratamientoService.obtenerTratamientoPorId(1L)).thenReturn(java.util.Optional.of(tratamiento1));

        // Act
        ResultActions response = mockMvc.perform(
                get("/tratamiento/find/1")
        );

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Tratamiento 1"))
                .andExpect(jsonPath("$.fecha").value("2023-10-01")
                );

    }

    @Test
    public void TratamientoController_mostrarTratamiento2_Tratamiento_NoEncontrado() throws Exception {

        // Arrange
        when(tratamientoService.obtenerTratamientoPorId(2L)).thenReturn(java.util.Optional.empty());

        // Act
        ResultActions response = mockMvc.perform(
                get("/tratamiento/find/2")
        );

        // Assert
        response.andExpect(status().isNotFound());

    }

    @Test
    public void TratamientoController_actualizarTratamiento1_Tratamiento_Encontrado() throws Exception {

        // Arrange
        Tratamiento tratamiento1 = new Tratamiento();
        tratamiento1.setId(1L);
        tratamiento1.setDescripcion("Tratamiento 1");
        tratamiento1.setFecha(LocalDate.of(2023, 10, 1));

        when(tratamientoService.obtenerTratamientoPorId(1L)).thenReturn(java.util.Optional.of(tratamiento1));
        when(tratamientoService.guardarTratamiento(Mockito.any(Tratamiento.class))).thenReturn(tratamiento1);

        // Act
        ResultActions response = mockMvc.perform(
                post("/tratamiento/update/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(tratamiento1))
        );

        // Assert
        response.andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Tratamiento 1"))
                .andExpect(jsonPath("$.fecha").value("2023-10-01")
                );


    }

}


/*
    Pruebas de integración: Por efecto práctico, son aquellas pruebas que simulan el llamado http que tengo que
    hacer.

 */