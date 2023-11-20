package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.dto.JwtAuthenticationResponse;
import com.adminParking.adminParking.dto.LoginDTO;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PisoTest {

	@LocalServerPort
    private int port;

    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    TipoVehiculoRepository tipoRepository;
    
    @Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private TestRestTemplate rest;

    @BeforeEach
    void init() {
        userRepository.save(
            new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.PORTERO));
        userRepository.save(
            new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.ADMIN));
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(tipo);
        pisoRepository.save(new PisoEntity("2000", tipo, 2000));
    }

    private JwtAuthenticationResponse login(String email, String password) {
		RequestEntity<LoginDTO> request = RequestEntity.post("http://localhost:" + port + "/auth/login")
				.body(new LoginDTO(email, password));
		ResponseEntity<JwtAuthenticationResponse> jwtResponse = rest.exchange(request, JwtAuthenticationResponse.class);
		JwtAuthenticationResponse body = jwtResponse.getBody();
		assertNotNull(body);
		return body;
	}

    @Test
    public void test_returns_all_piso_entities() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
    
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
    
        ResponseEntity<List<PisoEntity>> responseEntity = rest.exchange(
                "http://localhost:" + port + "/pisosRest/getPisos",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<PisoEntity>>() {}
        );
    
        List<PisoEntity> result = responseEntity.getBody();
        assertNotNull(result); // Verifica que la lista no sea nula
    
        if (!result.isEmpty()) {
            assertEquals("2000", result.get(0).getArea());
            assertEquals("Carro", result.get(0).getTipoVehiculo().getTipo());
            assertEquals(2000, result.get(0).getCapacidad());
        }
    }
    
    @Test
    public void test_getPisoById() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        Long pisoId = 1L;

        ResponseEntity<PisoEntity> responseEntity = rest.exchange(
                "http://localhost:" + port + "/pisosRest/" + pisoId,
                HttpMethod.GET,
                requestEntity,
                PisoEntity.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        PisoEntity result = responseEntity.getBody();
        assertNotNull(result); // Verifica que el resultado no sea nulo

        // Verifica los atributos del PisoEntity
        assertEquals("2000", result.getArea());
        assertEquals("Carro", result.getTipoVehiculo().getTipo());
        assertEquals(2000, result.getCapacidad());
    }

    @Test
    public void test_createPiso() {
        // Simular el inicio de sesión para obtener el token JWT
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Moto");
        tipo = tipoRepository.save(tipo); // Guarda el tipo de vehículo y obtiene el ID generado
    
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<PisoEntity> requestEntity = new HttpEntity<>(new PisoEntity("3000", tipo), headers);
    
        ResponseEntity<PisoEntity> responseEntity = rest.exchange(
                "http://localhost:" + port + "/pisosRest/",
                HttpMethod.POST,
                requestEntity,
                PisoEntity.class
        );
    
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    
        PisoEntity createdPiso = responseEntity.getBody();
        assertNotNull(createdPiso); // Verifica que el PisoEntity creado no sea nulo
    
        assertEquals("3000", createdPiso.getArea());
        assertEquals("Moto", createdPiso.getTipoVehiculo().getTipo());
    }
    


    @Test
    public void test_updateEspacios() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
    
        Long pisoId = 1L;
        PisoEntity updatedPiso = pisoRepository.findById(pisoId).orElse(null);
        int capacidadEsperada = updatedPiso.getCapacidad() - 1;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<Long> requestEntity = new HttpEntity<>(pisoId, headers);
    
        rest.postForObject("http://localhost:" + port + "/pisosRest/updateEspacios", requestEntity, Void.class);
    
        PisoEntity updatedPiso2 = pisoRepository.findById(pisoId).orElse(null);
        int capacidadNueva = updatedPiso2.getCapacidad();
    
        assertEquals(capacidadEsperada, capacidadNueva);
    }
    @Test
    public void test_salirVehiculoPiso() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
    
        Long pisoId = 1L;
        PisoEntity updatedPiso = pisoRepository.findById(pisoId).orElse(null);
        int capacidadEsperada = updatedPiso.getCapacidad() + 1;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        HttpEntity<Long> requestEntity = new HttpEntity<>(pisoId, headers);
    
        rest.postForObject("http://localhost:" + port + "/pisosRest/salirVehiculoPiso", requestEntity, Void.class);
        
        PisoEntity updatedPiso2 = pisoRepository.findById(pisoId).orElse(null);
        int capacidadNueva = updatedPiso2.getCapacidad();
    
        assertEquals(capacidadEsperada, capacidadNueva);
    }
}
