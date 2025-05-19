package com.example.whiskervet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.entity.Medicamento;
import com.example.whiskervet.servicio.medicamento.MedicamentoServiceImpl;

@RestController
@RequestMapping("/medicamento")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicamentoController {

    @Autowired
    private MedicamentoServiceImpl medicamentoService;

    @GetMapping("/find/{id}")
    public Medicamento mostrMedicamento(@PathVariable Long id){
        return medicamentoService.obtenerMedicamentoPorId(id).orElse(null);
    }

    @GetMapping("/all")
    public List<Medicamento> mostrarMedicamentos(){
        return medicamentoService.obtenerTodosLosMedicamentos();
    }

        @GetMapping("/all-0")
    public List<Medicamento> mostrarMedicamentosDisponibles(){
        return medicamentoService.obtenerTodosLosMedicamentosDisponibles();
    }

    @DeleteMapping("/delete/{id}")
    public void eliminarMedicamento(@PathVariable Long id){
        medicamentoService.eliminarMedicamento(id);
    }

    @GetMapping("/filtrar/{nombre}")
    public List<Medicamento> filtrarMedicamentosPorCedula(@PathVariable("nombre") String nombre) {
        System.out.println(medicamentoService.filtrarMedicamentosPorNombre(nombre));
        return medicamentoService.filtrarMedicamentosPorNombre(nombre);
    }
}
