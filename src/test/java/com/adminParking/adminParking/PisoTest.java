package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.controller.PisoRestController;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

import jakarta.transaction.Transactional;

import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

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
    
    @BeforeEach
    void init() {
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(tipo);
        pisoRepository.save(new PisoEntity("2000", tipo));
    }

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void test_returns_all_piso_entities() {
    
        ResponseEntity<List<PisoEntity>> responseEntity = rest.exchange(
            "http://localhost:" + port + "/pisosRest/getPisos",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<PisoEntity>>() {}
        );
        List<PisoEntity> result = responseEntity.getBody();
        // Compara los parámetros individualmente
        assertEquals("2000", result.get(0).getArea());
        assertEquals("Carro", result.get(0).getTipoVehiculo().getTipo());
        // Añade más comparaciones para otros parámetros según sea necesario
    }

    @Test
    public void test_getPisoById() {
        // ID del piso que deseas obtener
        Long pisoId = 1L;
        
        // Realiza una solicitud HTTP para obtener el PisoEntity por su ID
        ResponseEntity<PisoEntity> responseEntity = rest.exchange(
            "http://localhost:" + port + "/pisosRest/" + pisoId,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<PisoEntity>() {}
        );
        
        PisoEntity result = responseEntity.getBody();
        
        // Compara los parámetros individualmente
        assertEquals("2000", result.getArea());
        assertEquals("Carro", result.getTipoVehiculo().getTipo());
        // Añade más comparaciones para otros parámetros según sea necesario
    }

    @Test
    public void test_createPiso() {
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Moto");
        PisoEntity newPiso = new PisoEntity("3000", tipo);
        tipoRepository.save(tipo);
        PisoEntity piso = rest.postForObject("http://localhost:" + port + "/pisosRest/", newPiso, PisoEntity.class);
        assertEquals("3000", piso.getArea());
        assertEquals("Moto", piso.getTipoVehiculo().getTipo());
    }

}
