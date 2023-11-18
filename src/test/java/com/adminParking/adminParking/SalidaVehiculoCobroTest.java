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
import com.adminParking.adminParking.model.Cuenta;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;

@ActiveProfiles("systemtest")
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
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    private String baseUrl;

    @BeforeEach
    void init() {
        cuentaRepository.save(new Cuenta("cuenta1", "6000"));
        cuentaRepository.save(new Cuenta("cuenta2", "1000"));
        cuentaRepository.save(new Cuenta("cuenta3", "1000"));

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-extensions"); // disabling extensions
        options.addArguments("start-maximized"); // open Browser in maximized mode
        options.setBinary("C:\\Users\\camil\\chrome\\win64-114.0.5735.133\\chrome-win64\\chrome.exe");


        this.driver = new ChromeDriver(options);

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        this.baseUrl = "http://localhost:4200";
    }

    private void saldoDebeSer(String saldo) {
        String textoSaldoEsperado = String.format("Saldo: %s", saldo);
        WebElement liCuentaSaldo = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("liCuentaSaldo")));
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(liCuentaSaldo, textoSaldoEsperado));
        } catch (TimeoutException e) {
            fail("Could not find " + textoSaldoEsperado + ", instead found " + liCuentaSaldo.getText(), e);
        }
    }

    @AfterEach
    void end() {
        // driver.close();
        driver.quit();
    }

    @Test
    void saldo() {
        driver.get(baseUrl + "/cuenta/1");
        saldoDebeSer("6,000.00");
    }
}
