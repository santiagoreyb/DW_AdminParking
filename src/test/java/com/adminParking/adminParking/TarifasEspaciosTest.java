package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class TarifasEspaciosTest {
    private ChromeDriver driver;
    private WebDriverWait wait;

    
    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    TipoVehiculoRepository tipoRepository;

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    private TarifaRepository tarifaRepository; 
    
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

        VehiculoEntity vehiculo = new VehiculoEntity(obtenerFechaFormateada(18, 11, 2023, 12, 0),"xxx", tipo, piso);
        vehiculoRepository.save(vehiculo);

        TarifaEntity tarifaCarro = new TarifaEntity();
        tarifaCarro.setTipoVehiculo(tipo);
        tarifaCarro.setTarifaPorMinuto(900);
        tarifaRepository.save(tarifaCarro);

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.setBinary("C:\\Users\\camil\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");

        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
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

    public String obtenerFechaFormateada(int dia, int mes, int anio, int hora, int minuto) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(anio, mes - 1, dia, hora, minuto); // El mes se resta en 1 porque en Calendar, enero es 0
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return formato.format(calendario.getTime());
    }
    
    public String obtenerFechaYHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date now = new Date();
        return formato.format(now);
    }

    public void retirarVehiculo(){
        driver.get(baseUrl + "/vehiculo/registrarSalida");
        WebElement placa = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("placa")));
        placa.sendKeys(Keys.BACK_SPACE);
        placa.sendKeys("xxx");
        WebElement btnCobro = driver.findElement(By.id("btnCobro"));
        btnCobro.click();
        WebElement btnRetirar = driver.findElement(By.id("btnRetirar"));
        btnRetirar.click();
        WebElement AvisoFinal = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mensajeRetiro")));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(AvisoFinal, "Vehículo retirado"));
        } catch (TimeoutException e) {
            fail("Could not find " + "Vehículo retirado" + ", instead found " + AvisoFinal.getText(), e);
        }
    }

    public void ingresarVehiculo(){
        PisoEntity pi = pisoRepository.findById(1L).orElse(null);
        pi.setCapacidad(pi.getCapacidad()-1);
        pisoRepository.save(pi);
    }

    @Test
    void verificarEspacios() {
        login();
        driver.get(baseUrl + "/tarifa/tarifas-espacios");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("pisos"), 1));
        List<WebElement> cuentas = driver.findElements(By.className("pisos"));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(cuentas.get(0), "Carro: 2000 espacios disponibles"));
        } catch (TimeoutException e) {
            fail("Could not find " + "Carro: 2000 espacios disponibles" + ", instead found " +cuentas.get(0).getText(), e);
        }
    }

    @Test
    void verificarEspaciosDespuesDeIngresar() {
        login();
        ingresarVehiculo();
        driver.get(baseUrl + "/tarifa/tarifas-espacios");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("pisos"), 1));
        List<WebElement> cuentas = driver.findElements(By.className("pisos"));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(cuentas.get(0), "Carro: 1999 espacios disponibles"));
        } catch (TimeoutException e) {
            fail("Could not find " + "Carro: 1999 espacios disponibles" + ", instead found " +cuentas.get(0).getText(), e);
        }
    }

    @Test
    void verificarEspaciosDespuesDeRetirada() {
        login();
        retirarVehiculo();
        driver.get(baseUrl + "/tarifa/tarifas-espacios");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("pisos"), 1));
        List<WebElement> cuentas = driver.findElements(By.className("pisos"));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(cuentas.get(0), "Carro: 2001 espacios disponibles"));
        } catch (TimeoutException e) {
            fail("Could not find " + "Carro: 2001 espacios disponibles" + ", instead found " +cuentas.get(0).getText(), e);
        }
    }

    @Test
    void verificarTarifas() {
        login();
        driver.get(baseUrl + "/tarifa/tarifas-espacios");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.className("tarifas"), 1));
        List<WebElement> cuentas = driver.findElements(By.className("tarifas"));
        assertEquals("Carro: $900 por minuto", cuentas.get(0).getText());
    }



}
