package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.controller.TarifaController;
import com.adminParking.adminParking.dto.JwtAuthenticationResponse;
import com.adminParking.adminParking.dto.LoginDTO;
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
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class tarifaRestTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
	private PisoRepository pisoRepository;

    @Autowired
    VehiculoRepository vehiculoRepository;
    
    @Autowired
    TipoVehiculoRepository tipoRepository;

     @Autowired
    TarifaRepository tarifaRepository;


     @BeforeEach
    void init() {
        userRepository.save(new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.PORTERO));
        userRepository.save(new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.ADMIN));
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Moto");
        tipoRepository.save(tipo);
        PisoEntity piso = new PisoEntity("2000", tipo, 2000);
        pisoRepository.save(piso);
        TarifaEntity tarifaCarro = new TarifaEntity();
        tarifaCarro.setTipoVehiculo(tipo);
        tarifaCarro.setTarifaPorMinuto(900);
        tarifaRepository.save(tarifaCarro);
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
    public void testGetAllTarifas() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                "http://localhost:" + port + "/tarifasRest/getTarifas",
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetTarifaById() {
        Long id = 1L;

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<TarifaEntity> response = restTemplate.exchange(
                "http://localhost:" + port + "/tarifasRest/" + id,
                HttpMethod.GET,
                requestEntity,
                TarifaEntity.class
        );

        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }


    @Test
    public void testDeleteTarifa() {
        Long id = 1L;

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        
        System.out.println("User roles: " + bob.getRole()); 
        
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/tarifasRest/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class
        );
        
        System.out.println("Response status code: " + response.getStatusCodeValue()); 
        
        assertEquals(200, response.getStatusCodeValue());
    }



    @Test
    public void testUpdateTarifa() {
        Long id = 1L;
        TarifaEntity existingTarifa = tarifaRepository.findById(id).orElse(null);

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        
        ResponseEntity<TarifaEntity> response = restTemplate.exchange(
                "http://localhost:" + port + "/tarifasRest/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(existingTarifa, headers),
                TarifaEntity.class
        );
        assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }


}
