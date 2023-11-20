
package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

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

import com.adminParking.adminParking.dto.JwtAuthenticationResponse;
import com.adminParking.adminParking.dto.LoginDTO;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;

// mvn test -Dtest=TipoVehiculoTests
@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TipoVehiculoTest {

    @LocalServerPort
    private int port ;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @BeforeEach
    void init() {
        userRepository.save(new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.PORTERO));
        userRepository.save(new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.ADMIN));
        TipoVehiculoEntity tipoVehiculoEntity = new TipoVehiculoEntity("Carro");
        tipoVehiculoRepository.save ( tipoVehiculoEntity ) ;
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
    public void test_getAllTarifas ( ) {

        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/tiposvehiculoRest/getTipos", List.class);
        List<TipoVehiculoEntity> listaTiposVehiculo = response.getBody();
        assertEquals(null, listaTiposVehiculo);

    }

    @Test
    public void testGetTipoVehiculoById() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        Long tipoId = 1L ;
        ResponseEntity<TipoVehiculoEntity> response = restTemplate.getForEntity("http://localhost:" + port + "/tiposvehiculoRest/" + tipoId, TipoVehiculoEntity.class);
        TipoVehiculoEntity IdTiposVehiculo = response.getBody();
        assertEquals(null, IdTiposVehiculo);
    }


    @Test
    public void testCreateTipoVehiculo() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");

        String tipoNombre = "Motoneta";
        HttpEntity<String> requestEntity = new HttpEntity<>(tipoNombre, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/tiposvehiculoRest/anadirTipoVehiculo",
                requestEntity,
                Void.class
        );

        assertEquals(200, response.getStatusCodeValue());

        // Retrieve the created TipoVehiculoEntity
        Optional<TipoVehiculoEntity> createdTipoVehiculoOptional = tipoVehiculoRepository.findByTipo(tipoNombre);
        assertTrue(createdTipoVehiculoOptional.isPresent());

        // Now you can get the TipoVehiculoEntity from the Optional
        TipoVehiculoEntity createdTipoVehiculo = createdTipoVehiculoOptional.get();
        assertNotNull(createdTipoVehiculo);
        assertEquals(tipoNombre, createdTipoVehiculo.getTipo());
    }


    @Test
    public void testDeleteTipoVehiculo() {
        JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + bob.getToken());

        TipoVehiculoEntity tipoVehiculoToDelete = new TipoVehiculoEntity("TipoToDelete");
        tipoVehiculoRepository.save(tipoVehiculoToDelete);

        Long tipoIdToDelete = tipoVehiculoToDelete.getId();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/tiposvehiculoRest/deleteTipoVehiculo/" + tipoIdToDelete,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        assertEquals(200, response.getStatusCodeValue());

        Optional<TipoVehiculoEntity> deletedTipoVehiculoOptional = tipoVehiculoRepository.findById(tipoIdToDelete);
        assertFalse(deletedTipoVehiculoOptional.isPresent());
    }

}
