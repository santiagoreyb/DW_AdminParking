package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

import io.ous.jtoml.ParseException;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class CrudPisosTest {
    

    private ChromeDriver driver;
    private WebDriverWait wait;
    
    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    TipoVehiculoRepository tipoRepository;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    private String baseUrl;

    @BeforeEach
    void init() {
        
        userRepository.save(new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.PORTERO));
        userRepository.save(new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.ADMIN));

        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(tipo);
        PisoEntity piso = new PisoEntity("2000", tipo, 2000);
        pisoRepository.save(piso);

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("start-maximized"); // open Browser in maximized mode
        // options.setBinary("C:\\Users\\camil\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.baseUrl = "http://localhost:4200";

    }

    @AfterEach
    void end() {
        // driver.close();
        driver.quit();
    }

    void login() {

        String email = "bob@bob.com";
        String password = "bob123";
        driver.get(baseUrl + "/login");
        WebElement txtEmail = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txtEmail")));
        txtEmail.sendKeys(Keys.BACK_SPACE);
        txtEmail.sendKeys(email);
        WebElement txtPassword = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("txtPassword")));
        txtPassword.sendKeys(Keys.BACK_SPACE);
        txtPassword.sendKeys(password);
        WebElement btnLogin = driver.findElement(By.id("btnLogin"));
        btnLogin.click();
        try {
            wait.until(ExpectedConditions.urlToBe("http://localhost:4200/vehiculo/anadirVehiculo"));
        } catch (TimeoutException e) {
            fail("Could not find the expected URL: http://localhost:4200/vehiculo/anadirVehiculo", e);
        }

	}

    @Test
    void test_createPiso ( ) {

        login();

        driver.get(baseUrl + "/piso/anadirPiso");

        WebElement areaInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("area")));
        WebElement tipoVehiculoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tipoVehiculo")));
        WebElement areaPorVehiculoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("areaPorVehiculo")));

        areaInput.sendKeys("2000");
        tipoVehiculoInput.sendKeys("Carro");
        areaPorVehiculoInput.sendKeys("100");

        WebElement btnCrearPiso = driver.findElement(By.className("botoon")); // Encuentra el botón por la clase CSS
        btnCrearPiso.click();

        try {
            WebElement mensajeExito = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Piso añadido exitosamente.')]")));
            assertNotNull(mensajeExito);
        } catch (TimeoutException e) {
            fail("La creación del piso no se completó correctamente.", e);
        }

    }

    @Test
    void test_updatePiso ( ) {
    
        login();
    
        driver.get(baseUrl + "/piso/actualizarPiso");
    
        WebElement idInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("id")));
        WebElement areaInput = driver.findElement(By.id("area"));
        WebElement tipoVehiculoInput = driver.findElement(By.id("tipoVehiculo"));
        WebElement areaPorVehiculoInput = driver.findElement(By.id("areaPorVehiculo"));
    
        idInput.sendKeys("1"); // Cambia esto por el ID del piso que deseas actualizar
        areaInput.sendKeys("500"); // Nueva área del piso
        tipoVehiculoInput.sendKeys("Carro"); // Nuevo tipo de vehículo
        areaPorVehiculoInput.sendKeys("5"); // Nueva área por vehículo
    
        WebElement btnActualizarPiso = driver.findElement(By.className("botoon"));
        btnActualizarPiso.click();
    
        try {
            WebElement mensajeExito = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Piso actualizado exitosamente.')]")));
            assertNotNull(mensajeExito);
        } catch (TimeoutException e) {
            fail("La actualización del piso no se completó correctamente.", e);
        }

    }

    /*
    @Test
    void test_actualizarPiso ( ) {
    
        login();
    
        driver.get(baseUrl + "/piso/actu");
    
        WebElement idInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("id")));
        WebElement areaInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("area")));
        WebElement tipoVehiculoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tipoVehiculo")));
        WebElement areaPorVehiculoInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("areaPorVehiculo")));
    
        // Ingresa la información del piso que deseas actualizar
        idInput.sendKeys("1"); // Reemplaza con el ID del piso que quieres actualizar
        areaInput.sendKeys("2500"); // Reemplaza con el nuevo valor del área
        tipoVehiculoInput.sendKeys("Camioneta"); // Reemplaza con el nuevo tipo de vehículo
        areaPorVehiculoInput.sendKeys("150"); // Reemplaza con la nueva área por vehículo
    
        WebElement btnActualizarPiso = driver.findElement(By.className("botoon"));
        btnActualizarPiso.click();
    
        try {
            WebElement mensajeExito = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Piso actualizado exitosamente.')]")));
            assertNotNull(mensajeExito);
        } catch (TimeoutException e) {
            fail("La actualización del piso no se completó correctamente.", e);
        }
    
    }
    */

    @Test
    void deletePisoTest() {
    
        login();
    
        driver.get(baseUrl + "/piso/eliminarPiso");
    
        WebElement idInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("id")));
        idInput.sendKeys("1"); // Cambia esto por el ID del piso que deseas eliminar
    
        WebElement btnEliminarPiso = driver.findElement(By.className("botoon"));
        btnEliminarPiso.click();
    
        try {
            WebElement mensajeExito = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(text(),'Piso eliminado exitosamente.')]")));
            assertNotNull(mensajeExito);
        } catch (TimeoutException e) {
            fail("La eliminación del piso no se completó correctamente.", e);
        }
    
    }
    

}
