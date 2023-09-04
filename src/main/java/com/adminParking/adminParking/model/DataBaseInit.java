package com.adminParking.adminParking.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@Component
public class DataBaseInit implements ApplicationRunner {

    @Autowired
    private PisoRepository pisoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

     @Autowired
    private TarifaRepository tarifaRepository; 


    @Override
    public void run(ApplicationArguments args) throws Exception {
          // Coloca aquí el código que deseas ejecutar al iniciar la aplicación.
        // Por ejemplo, la inicialización de datos.
        PisoEntity piso = new PisoEntity("Piso A","Carro");
        pisoRepository.save(piso);

        // Inicializar tarifas
        TarifaEntity tarifaCarro = new TarifaEntity();
        tarifaCarro.setTipoVehiculo("Carro");
        tarifaCarro.setTarifaPorMinuto(0.1); // Establece la tarifa por minuto para carros

        TarifaEntity tarifaMoto = new TarifaEntity();
        tarifaMoto.setTipoVehiculo("Moto");
        tarifaMoto.setTarifaPorMinuto(0.05); // Establece la tarifa por minuto para motos

        // Guardar las tarifas en la base de datos
        tarifaRepository.save(tarifaCarro);
        tarifaRepository.save(tarifaMoto);

        VehiculoEntity vehiculo = new VehiculoEntity("10:00 AM", "12:00 PM","eee","Carro");
        vehiculo.setPiso(piso);
        vehiculo.setTarifa(tarifaCarro);
        //piso.getVehiculos().add(vehiculo);
        vehiculoRepository.save(vehiculo);
        
    }
}
