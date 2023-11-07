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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.adminParking.adminParking.controller.PisoRestController;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
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
        // Datos que se agregaron en el método init
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        PisoEntity expectedPiso = new PisoEntity("2000", tipo);
    
        ResponseEntity<List<PisoEntity>> responseEntity = rest.exchange(
            "http://localhost:" + port + "/pisosRest/getPisos",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<PisoEntity>>() {}
        );
        List<PisoEntity> result = responseEntity.getBody();
    
        // Compara los parámetros individualmente
        assertEquals(expectedPiso.getArea(), result.get(0).getArea());
        assertEquals(expectedPiso.getTipoVehiculo().getTipo(), result.get(0).getTipoVehiculo().getTipo());
        // Añade más comparaciones para otros parámetros según sea necesario
    }
    
    
    


}
