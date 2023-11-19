
package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

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
    TipoVehiculoRepository tipoVehiculoRepository ;

    @BeforeEach
    void init() {
        TipoVehiculoEntity tipoVehiculoEntity = new TipoVehiculoEntity("Carro");
        tipoVehiculoRepository.save ( tipoVehiculoEntity ) ;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoVehiculoTest.class);

    @Test
    public void test_getAllTarifas ( ) {
        ResponseEntity<List> response = restTemplate.getForEntity("http://localhost:" + port + "/tiposvehiculoRest/getTipos", List.class);
        List<TipoVehiculoEntity> listaTiposVehiculo = response.getBody();
        LOGGER.info("Response Body: {}", listaTiposVehiculo);
        assertEquals(null, listaTiposVehiculo);
    }

}
