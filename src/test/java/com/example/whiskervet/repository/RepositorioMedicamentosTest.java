package com.example.whiskervet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.whiskervet.entity.Medicamento;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
public class RepositorioMedicamentosTest {

    @Autowired
    private RepositorioMedicamentos repositorioMedicamentos;

        @BeforeEach
    void init(){
        repositorioMedicamentos.save(new Medicamento("ACOLAN", 50, 10, 10, 2));
        repositorioMedicamentos.save(new Medicamento("ACTIONIS", 20, 12, 30, 0));
        repositorioMedicamentos.save(new Medicamento("ACUIMIX", 150, 100, 0, 20));
        repositorioMedicamentos.save(new Medicamento("ADVOCIN", 100, 80, 15, 5));
        repositorioMedicamentos.save(new Medicamento("AEROFAR", 50, 30, 0, 5));

    }

    @Test
    public void RepositorioMedicamentosTest_calcularVentasTotales_debeRetornarLaSumaDeLasVentas() {
        // 1. Arrange
        //2. Act
        Float ventasTotales = repositorioMedicamentos.calcularVentasTotales();
        // 3. Assert
        assertNotNull(ventasTotales);
        assertEquals(3850.0f, ventasTotales, 0.0f);
    }

    @Test
    public void RepositorioMedicamentosTest_calcularGananciasTotales_debeRetornarLaSumaDeLasGanancias() {
        // 1. Arrange
        //2. Act
        Float gananciasTotales = repositorioMedicamentos.calcularGananciasTotales();
        // 3. Assert
        assertNotNull(gananciasTotales);
        assertEquals(1280.0f, gananciasTotales, 0.0f);
    }

    @Test
    public void RepositorioMedicamentosTest_findByNombreContaining_debeRetornarLosMedicamentosQueContienenElNombre() {
        // 1. Arrange
        //2. Act
        List<Medicamento> medicamentos = repositorioMedicamentos.findByNombreContaining("AC");
        // 3. Assert
        assertNotNull(medicamentos);
        assertEquals(3, medicamentos.size());
    }

    @Test
    public void RepositorioMedicamentosTest_findByUnidadesDisponiblesGreaterThanZero_debeRetornarLosMedicamentosConUnidadesDisponibles() {
        // 1. Arrange
        //2. Act
        List<Medicamento> medicamentos = repositorioMedicamentos.findByUnidadesDisponiblesGreaterThanZero();
        // 3. Assert
        assertNotNull(medicamentos);
        assertEquals(3, medicamentos.size());
    }

}