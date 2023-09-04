package com.adminParking.adminParking.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@Component
public class DataBaseInit implements ApplicationRunner {

    @Autowired
    private PisoRepository pisoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
          // Coloca aquí el código que deseas ejecutar al iniciar la aplicación.
        // Por ejemplo, la inicialización de datos.
        PisoEntity piso = new PisoEntity("Piso A");
        pisoRepository.save(piso);

        VehiculoEntity vehiculo = new VehiculoEntity("10:00 AM", "12:00 PM");
        vehiculo.setPiso(piso);
        //piso.getVehiculos().add(vehiculo);
        vehiculoRepository.save(vehiculo);
    }
}
