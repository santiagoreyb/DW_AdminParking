package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import com.adminParking.adminParking.dto.JwtAuthenticationResponse;
import com.adminParking.adminParking.dto.LoginDTO;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.repositories.UserRepository;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SecurityTest {
    
    @LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
	private TestRestTemplate rest;

	@BeforeEach
	void init() {
		userRepository.save(
				new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.ADMIN));
		userRepository.save(
				new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.PORTERO));

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
	void userData() {
		JwtAuthenticationResponse alice = login("alice@alice.com", "alice123");
		JwtAuthenticationResponse bob = login("bob@bob.com", "bob123");

		assertEquals("alice@alice.com", alice.getEmail());
		assertEquals("bob@bob.com", bob.getEmail());
	}
}
