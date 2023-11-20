package com.adminParking.adminParking;

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
public class SalidaVehiculoCobroTest {
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
        //options.setBinary("C:\\Users\\camil\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");
        options.setBinary("C:\\Users\\kevin\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");
        this.driver = new ChromeDriver(options);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        this.baseUrl = "http://localhost:4200";
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
    void verificarCobroTest() {
        login();
        driver.get(baseUrl + "/vehiculo/registrarSalida");
        WebElement placa = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("placa")));
        placa.sendKeys(Keys.BACK_SPACE);
        placa.sendKeys("xxx");
        WebElement btnCobro = driver.findElement(By.id("btnCobro"));
        btnCobro.click();
        String cobro = calcularTarifa(obtenerFechaFormateada(18, 11, 2023, 12, 0), obtenerFechaYHoraActual());
        cobroDebeSer(cobro);
    }

    @Test
    void verificarSalidaTest() {
        login();
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

    private void cobroDebeSer(String cobro) {

        WebElement cobroFinal = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("cobroFinal")));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(cobroFinal, cobro));
        } catch (TimeoutException e) {
            fail("Could not find " + cobro + ", instead found " + cobroFinal.getText(), e);
        }
    }
    
    
    public Date parsearFecha(String fechaStr) {
        String[] partes = fechaStr.split(" ");
        String fecha = partes[0];
        String hora = partes[1];
        String[] fechaArray = fecha.split("/");
        String[] horaArray = hora.split(":");

        int dia = Integer.parseInt(fechaArray[0]);
        int mes = Integer.parseInt(fechaArray[1]) - 1; // El mes se resta en 1 porque en Calendar, enero es 0
        int anio = Integer.parseInt(fechaArray[2]);
        int horaStr = Integer.parseInt(horaArray[0]);
        int minutoStr = Integer.parseInt(horaArray[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(anio, mes, dia, horaStr, minutoStr);
        return calendar.getTime();
    }

    public String calcularTarifa(String tiempoLlegada, String tiempoSalida) {
        double cobroFinal = 0;
        try {
            Date llegada = parsearFecha(tiempoLlegada);
            Date salida = parsearFecha(tiempoSalida);
            double tarifaMinuto = 900; // Ejemplo de tarifa por minuto
    
            System.out.println("Llegada: " + llegada);
            System.out.println("Salida: " + salida);
    
            if (llegada != null && salida != null) {
                long minutosTranscurridos = (salida.getTime() - llegada.getTime()) / (1000 * 60);
                cobroFinal = minutosTranscurridos * tarifaMinuto;
            } else {
                System.out.println("La hora de llegada o la hora de salida no son válidas.");
            }
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha: " + e.getMessage());
        }
        // Formatear el resultado como moneda
        return String.format("$%,.2f", cobroFinal).replace('.', '*').
                replace(',', '.').replace('*', ',');

    }
    

}
