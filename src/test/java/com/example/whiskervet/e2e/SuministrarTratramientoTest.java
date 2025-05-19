package com.example.whiskervet.e2e;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SuministrarTratramientoTest {
    
    private WebDriver webDriver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:4200/";

    @BeforeEach
    public void init() {
        // webDriver = new ChromeDriver();
        // webDriverWait = new WebDriverWait(webDriver, 10);
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        //chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-extensions");

        this.webDriver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    }

    @Test
    public void SystemTest_Tratamiento(){
        webDriver.get(BASE_URL);
        iniciarSesionAdministrador();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gananciasTotales")));  
        int ganaciasTotalesAntes = Integer.parseInt(webDriver.findElement(By.id("gananciasTotales")).getText().replaceAll("[^0-9]", ""));
        int tratamientosSuministradosAntes = Integer.parseInt(webDriver.findElement(By.id("tratamientosUltimoMes")).getText());

        webDriver.get(BASE_URL);
        iniciarSesionVeterinario();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("agregarTratamiento")));  
        // Dar click en el botón de agregar tratamiento
        webDriver.findElement(By.id("agregarTratamiento")).click();

        SuministrarTratramiento();

        verificarTratamientoGuardado();


        // Espera a que aparezca el componente app-top-bar-cliente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("app-top-bar-admin")));

        // Cerrar Sesión
        webDriver.findElement(By.id("cerrarSesion")).click();
        webDriver.get(BASE_URL);
        iniciarSesionAdministrador();

        int ganaciasTotalesDespues = Integer.parseInt(webDriver.findElement(By.id("gananciasTotales")).getText().replaceAll("[^0-9]", ""));
        int tratamientosSuministradosDespues = Integer.parseInt(webDriver.findElement(By.id("tratamientosUltimoMes")).getText());

        Assertions.assertTrue(ganaciasTotalesAntes < ganaciasTotalesDespues);
        Assertions.assertEquals(tratamientosSuministradosAntes + 1, tratamientosSuministradosDespues);
    }

    private void verificarTratamientoGuardado() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ultimaPagina")));
        // Supongamos que ya tienes un WebElement que representa la última página, puedes obtenerlo por su clase, ID, o algún otro selector.
        WebElement ultimaPaginaBoton = webDriver.findElement(By.id("ultimaPagina"));

        // Luego, haces clic en el botón de la última página
        ultimaPaginaBoton.click();
        // Encuentra la última fila de la tabla
        List<WebElement> filas = webDriver.findElements(By.xpath("//table[@class='tabla-general2']/tbody/tr"));
        WebElement ultimaFila = filas.get(filas.size() - 1);

        // Encuentra las celdas (columnas) en la última fila
        List<WebElement> celdas = ultimaFila.findElements(By.tagName("td"));

        // La fecha está en la cuarta celda y el nombre del medicamento está en la tercera celda
        WebElement celdaFecha = celdas.get(4);
        WebElement celdaNombreMedicamento = celdas.get(3);

        // Se obtiene el contenido de la fecha y el nombre del medicamento
        String fecha = celdaFecha.getText();
        String nombreMedicamento = celdaNombreMedicamento.getText();


        // Obtén la fecha actual en formato "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String fechaActual = dateFormat.format(date);

        // "fecha" contendrá la fecha y "nombreMedicamento" contendrá el nombre del medicamento en la última fila de la tabla.
        Assertions.assertEquals(fechaActual, fecha);
        Assertions.assertEquals("AMOXOIL", nombreMedicamento);

    }

    public void iniciarSesionVeterinario(){
        String cedula = "9876543210";
        String password = "veterinario2";

        //Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("iniciarSesion")).click();

        //Hacer click en el botón de inicio de sesión para veterinario
        webDriver.findElement(By.id("loginVeterinario")).click();

        //Ingresar cédula y contraseña
        webDriver.findElement(By.id("inputCedula")).sendKeys(cedula);
        webDriver.findElement(By.id("inputPassword")).sendKeys(password);

        //Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("btnIngresarVeterinario")).click();
    }

    public void SuministrarTratramiento(){
        //Ingresar nombre de la mascota
        webDriver.findElement(By.id("mascotaId")).sendKeys("Selle");
        
        // Esperar a que se cargue la lista de resultados utilizando WebDriverWait
        WebElement resultados = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mascotaId")));
        
        // Mascota buscada
        String mascotaBuscada = "Selle";

        // Encuentra y selecciona la mascota por nombre
        WebElement mascotaSeleccionado = resultados.findElement(By.xpath("//option[contains(text(), '" + mascotaBuscada + "')]"));
        mascotaSeleccionado.click();

        String veterinarioBuscado = "9876543210";
        
        //Ingresar cédula del veterinario
        webDriver.findElement(By.id("veterinarioId")).sendKeys(veterinarioBuscado);
        
        // Esperar a que se cargue la lista de resultados utilizando WebDriverWait
        resultados = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("veterinarioId")));

        // Encuentra y selecciona el veterinario
        WebElement veterinarioSeleccionado = resultados.findElement(By.xpath("//option[contains(text(), '" + veterinarioBuscado + "')]"));
        veterinarioSeleccionado.click();

        String medicamentoBuscado = "AMOXOIL";
        //Ingresar cédula del veterinario
        webDriver.findElement(By.id("medicamentoId")).sendKeys("AMOX");

        // Esperar a que se cargue la lista de resultados utilizando WebDriverWait
        resultados = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("medicamentoId")));
        
        // Encuentra y selecciona el medicamento
        WebElement medicamentoSeleccionado = resultados.findElement(By.xpath("//option[contains(text(), '" + medicamentoBuscado + "')]"));
        medicamentoSeleccionado.click();

        //Ingresar descripción
        webDriver.findElement(By.id("descripcion")).sendKeys("Se suministra un tratamiento a la mascota");
        
        //Clickear botón de guardar
        webDriver.findElement(By.id("btnGuardarTratamiento")).click();
    }

    public void iniciarSesionAdministrador(){
        String cedula = "123456789";
        String password = "12345";

        //Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("iniciarSesion")).click();

        //Hacer click en el botón de inicio de sesión para administrador
        webDriver.findElement(By.id("loginAdministrador")).click();

        //Ingresar cédula y contraseña
        webDriver.findElement(By.id("inputCedula")).sendKeys(cedula);
        webDriver.findElement(By.id("inputPassword")).sendKeys(password);

        //Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("btnIngresarAdministrador")).click();
    }


}
