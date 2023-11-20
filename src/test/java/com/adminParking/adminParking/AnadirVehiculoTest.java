package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

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

import org.openqa.selenium.support.ui.Select;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

    @ActiveProfiles("integrationtest")
    @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
    @SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class AnadirVehiculoTest {
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

        TarifaEntity tarifaCarro = new TarifaEntity();
        tarifaCarro.setTipoVehiculo(tipo);
        tarifaCarro.setTarifaPorMinuto(900);
        tarifaRepository.save(tarifaCarro);

        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless");
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.setBinary("C:\\Users\\camil\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");
        //options.setBinary("C:\\Users\\kevin\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");
        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.baseUrl = "http://localhost:4200";
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
    void anadirVehiculoAPiso() {
        login();
        driver.get(baseUrl + "/vehiculo/anadirVehiculo");

        // Esperar a que el elemento tipoVehiculo sea visible antes de seleccionarlo
        WebElement tipoVehiculoDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tipoVehiculo")));

        // Seleccionar un tipo de vehículo
        Select tipoVehiculoSelect = new Select(tipoVehiculoDropdown);
        tipoVehiculoSelect.selectByVisibleText("Carro");

        // Esperar a que el elemento idPiso sea interactivo después de seleccionar el tipo de vehículo
        WebElement placaInput = driver.findElement(By.id("placa"));
        placaInput.sendKeys("EMR040");


        // Aumenta el tiempo de espera si es necesario
        WebElement idPisoDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("idPiso")));
        Select idPisoSelect = new Select(idPisoDropdown);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[contains(text(),'ID del piso: 1 - Espacios disponibles: 2000')]")));

        idPisoSelect.selectByVisibleText("ID del piso: 1 - Espacios disponibles: 2000");
        // Hacer clic en el botón de submit
        WebElement submitButton = driver.findElement(By.id("btnIngresar"));
        idPisoSelect.selectByIndex(1);
        submitButton.click();
        
        WebElement AvisoFinal = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mensaje")));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(AvisoFinal, "El vehículo se guardó correctamente."));
        } catch (TimeoutException e) {
            fail("Could not find " + "El vehículo se guardó correctamente." + ", instead found " + AvisoFinal.getText(), e);
        }
        
    }

    @AfterEach
    void end() {
        // driver.close();
        driver.quit();
    }

}
