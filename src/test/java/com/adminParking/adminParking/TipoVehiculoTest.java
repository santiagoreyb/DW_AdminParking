
package com.adminParking.adminParking;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

// mvn test -Dtest=TipoVehiculoTests
@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TipoVehiculoTest {

    @LocalServerPort
    private int port ;

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @Autowired
    private TestRestTemplate rest;

    @BeforeEach
    void init() {
        TipoVehiculoEntity tipoVehiculoEntity = new TipoVehiculoEntity("Carro");
        tipoVehiculoRepository.save ( tipoVehiculoEntity ) ;
    }



}
