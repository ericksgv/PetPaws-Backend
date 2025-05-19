package com.example.whiskervet.e2e;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AgregarUsuarioMascotaTest {

    private WebDriver webDriver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:4200/";

    @BeforeEach
    public void init() {
        // webDriver = new ChromeDriver();
        // webDriverWait = new WebDriverWait(webDriver, 10);
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();

        // chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-extensions");

        this.webDriver = new ChromeDriver(chromeOptions);
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
    }

    @Test
    public void SystemTest_Veterinario() {


        webDriver.get(BASE_URL);

        loginVeterinario();
        
        // Dar click en el botón de agregar tratamiento
        webDriver.findElement(By.id("agregarUsuario")).click();

        registroCliente();

        

        // Espera a que aparezca el componente app-top-bar-cliente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("app-top-bar-veterinario")));

        // Dar click en la lista de mascotas
        webDriver.findElement(By.id("listaMascotas")).click();

        agregarMascota();

        // Espera a que aparezca el componente app-top-bar-cliente
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("app-top-bar-veterinario")));

        // Cerrar Sesión
        webDriver.findElement(By.id("cerrarSesion")).click();

        iniciarSesionCliente();

        // Dar click en el botón para ver todas las mascotas
        webDriver.findElement(By.id("verMascotas")).click();

        // Encontrar todos los elementos que tienen la clase "nombre-mascota"
        List<WebElement> elementosNombreMascota = webDriver.findElements(By.className("nombre-mascota"));

        // Itera a través de los elementos y verifica si alguno de ellos tiene el nombre
        // deseado
        WebElement mascotaBuscada = null;
        for (WebElement elementoNombreMascota : elementosNombreMascota) {
            if (elementoNombreMascota.getText().equals("Firulais")) {
                mascotaBuscada = elementoNombreMascota;
                break;
            }
        }

        Assertions.assertThat(mascotaBuscada.getText()).isEqualTo("Firulais");
        
    }



    private void loginVeterinario() {

        String cedula = "123";
        String password = "veterinario2";

        // Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("iniciarSesion")).click();

        // Hacer click en el botón de inicio de sesión para veterinario
        webDriver.findElement(By.id("loginVeterinario")).click();

        // Ingresar cédula y contraseña
        webDriver.findElement(By.id("inputCedula")).sendKeys(cedula);
        webDriver.findElement(By.id("inputPassword")).sendKeys(password);

        // Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("btnIngresarVeterinario")).click();

        // Mensaje esperado de error
        String expectedMessage = "El usuario no fue encontrado. Intenta de nuevo.";

        // Esperar a que se muestre el mensaje de error en la página
        WebElement elementError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajeError")));

        // Obtener el mensaje de error de la página
        String recievedMessage = elementError.getText();

        // Verificar que el mensaje de error sea igual al mensaje esperado
        Assertions.assertThat(recievedMessage).isEqualTo(expectedMessage);

        // Cédula válida de un veterinario
        cedula = "9876543210";

        // Ingresar cédula y contraseña
        webDriver.findElement(By.id("inputCedula")).clear();
        webDriver.findElement(By.id("inputCedula")).sendKeys(cedula);

        // Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("btnIngresarVeterinario")).click();
    }

        private void registroCliente() {
        String expectedMessage = "";
        // Ingresar los datos del cliente a agregar
        webDriver.findElement(By.id("cedula")).sendKeys("1000123456");
        webDriver.findElement(By.id("nombre")).sendKeys("Erick Garavito");
        webDriver.findElement(By.id("correo")).sendKeys("erick@gmailcom");
        webDriver.findElement(By.id("celular")).sendKeys("123456789");

        // Dar click en el botón de agregar cliente
        webDriver.findElement(By.id("btnAgregarCliente")).click();

        // Mensaje esperado de error
        expectedMessage = "Por favor, complete todos los campos requeridos y asegúrese de que el formato del correo sea válido.";

        // Esperar a que se muestre el mensaje de error en la página
        WebElement elementError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mensajeError")));

        // Obtener el mensaje de error de la página
        String recievedMessage = elementError.getText();

        // Verificar que el mensaje de error sea igual al mensaje esperado
        Assertions.assertThat(recievedMessage).isEqualTo(expectedMessage);

        // Corregir el formato del correo
        webDriver.findElement(By.id("correo")).clear();
        webDriver.findElement(By.id("correo")).sendKeys("erick@gmail.com");

        // Dar click en el botón de agregar cliente
        webDriver.findElement(By.id("btnAgregarCliente")).click();
    }

    public void agregarMascota(){
        // Dar click en el botón de agregar mascota
        webDriver.findElement(By.id("agregarMascota")).click();

        // Ingresar la información del dueño
        webDriver.findElement(By.id("duenoId")).sendKeys("1000");
        // Esperar a que se cargue la lista de resultados utilizando WebDriverWait
        WebElement resultados = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lista-de-duenios")));
        // Cédula buscada
        String cedulaDueño = "1000123456";
        // Encuentra y selecciona al dueño por su cédula
        WebElement dueñoSeleccionado = resultados
                .findElement(By.xpath("//option[contains(text(), '" + cedulaDueño + "')]"));
        dueñoSeleccionado.click();

        // Ingresar los datos de la mascota a agregar
        webDriver.findElement(By.id("nombre")).sendKeys("Firulais");
        webDriver.findElement(By.id("edad")).sendKeys("3");
        webDriver.findElement(By.id("foto")).sendKeys(
                "https://www.williamwalker.de/cdn/shop/articles/friendsofwilliamwalker_BreedProfileYorkshireTerrier4zu3.jpg?v=1628603961");
        webDriver.findElement(By.id("raza")).sendKeys("Yorkie");
        webDriver.findElement(By.id("enfermedad")).sendKeys("Ninguna");
        webDriver.findElement(By.id("peso")).sendKeys("3");
        String estadoSeleccionado = "hospitalizado";
        WebElement estadoSelect = webDriver.findElement(By.id("estado"));
        Select selectEstado = new Select(estadoSelect);
        selectEstado.selectByValue(estadoSeleccionado);

        // Clickear en Agregar Mascota
        webDriver.findElement(By.id("btnAgregarMascota")).click();
    }

    public void iniciarSesionCliente(){
        String cedula = "1000123456";
        // Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("iniciarSesion")).click();

        // Ingresar la cédula del cliente
        webDriver.findElement(By.id("inputCedula")).sendKeys(cedula);

        // Dar click en el botón de iniciar sesión
        webDriver.findElement(By.id("btnIngresarCliente")).click();
    }
}
