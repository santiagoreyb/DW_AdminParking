package com.adminParking.adminParking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@ActiveProfiles("integrationtest")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class VehiculoTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    VehiculoRepository vehiculoRepository;
    
    @Autowired
    TipoVehiculoRepository tipoRepository;

    @BeforeEach
    void init() {
        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        tipoRepository.save(tipo);
        VehiculoEntity vehiculo = new VehiculoEntity(obtenerFechaYHoraActual(), obtenerFechaYHoraActual(),"xxx", tipo);
        vehiculoRepository.save(vehiculo);
    }

    @Autowired
    private TestRestTemplate rest;

    @Test
    public void test_sacarVehiculoPiso() {
        Long vehiculoId = 1L;
        rest.postForObject("http://localhost:" + port + "/vehiculos/sacarVehiculoPiso", vehiculoId, void.class);
        VehiculoEntity vehi = vehiculoRepository.findById(vehiculoId).orElse(null);
        assertEquals(null, vehi.getPiso());
    }

    public String obtenerFechaYHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date now = new Date();
        return formato.format(now);
    }
    
}
