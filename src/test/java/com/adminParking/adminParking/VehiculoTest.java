
package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.dto.JwtAuthenticationResponse;
import com.adminParking.adminParking.dto.LoginDTO;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VehiculoTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    VehiculoRepository vehiculoRepository;
    
    @Autowired
    TipoVehiculoRepository tipoRepository;

    public String obtenerFechaYHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date now = new Date();
        return formato.format(now);
    }

    @BeforeEach
    void init() {
        userRepository.save(new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.PORTERO));
        userRepository.save(new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.ADMIN));
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(tipo);
        VehiculoEntity vehiculo = new VehiculoEntity(obtenerFechaYHoraActual(), obtenerFechaYHoraActual(),"xxx", tipo);
        vehiculoRepository.save(vehiculo);
    }
    
    private JwtAuthenticationResponse login(String email, String password) {
		RequestEntity<LoginDTO> request = RequestEntity.post("http://localhost:" + port + "/auth/login")
				.body(new LoginDTO(email, password));
		ResponseEntity<JwtAuthenticationResponse> jwtResponse = restTemplate.exchange(request, JwtAuthenticationResponse.class);
		JwtAuthenticationResponse body = jwtResponse.getBody();
		assertNotNull(body);
		return body;
	}

    @Test
    public void test_sacarVehiculoPiso() {
        Long vehiculoId = 1L;
        restTemplate.postForObject("http://localhost:" + port + "/vehiculos/sacarVehiculoPiso", vehiculoId, void.class);
        VehiculoEntity vehi = vehiculoRepository.findById(vehiculoId).orElse(null);
        assertEquals(null, vehi.getPiso());
    }

    @Test
    public void test_getAllVehiculos() {

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        ResponseEntity<List<VehiculoEntity>> response = restTemplate.exchange (
            "http://localhost:" + port + "/vehiculos/verVehiculos", HttpMethod.GET,
            requestEntity,
            new ParameterizedTypeReference<List<VehiculoEntity>>() {}
        );

        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());

        List<VehiculoEntity> vehiculos = response.getBody();
        assertNotNull(vehiculos);

    }

    @Test
    public void test_getVehiculoById() {

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        Long vehiculoId = 1L;

        ResponseEntity<VehiculoEntity> response = restTemplate.exchange(
            "http://localhost:" + port + "/vehiculos/" + vehiculoId,
            HttpMethod.GET,
            requestEntity,
            VehiculoEntity.class
        );

        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());

        VehiculoEntity vehiculo = response.getBody();
        assertNotNull(vehiculo);

    }

    @Test
    public void test_createVehiculo() {
    
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
    
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
    
        TipoVehiculoEntity nuevotipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(nuevotipo);
        VehiculoEntity nuevoVehiculo = new VehiculoEntity(obtenerFechaYHoraActual(), obtenerFechaYHoraActual(),"xxx", nuevotipo);
    
        HttpEntity<VehiculoEntity> requestEntity = new HttpEntity<>(nuevoVehiculo, headers);
    
        ResponseEntity<Void> response = restTemplate.exchange(
            "http://localhost:" + port + "/vehiculos/",
            HttpMethod.POST,
            requestEntity,
            Void.class
        );
    
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCodeValue());
        
    }
    
    
}
