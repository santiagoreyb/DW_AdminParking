package com.adminParking.adminParking.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@Component
public class DBInitializer implements ApplicationRunner {

    @Autowired
    private PisoRepository pisoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

     @Autowired
    private TarifaRepository tarifaRepository; 
    
    @Autowired
    private AdministradorRepository administradorRepository; 


    @Override
    public void run(ApplicationArguments args) throws Exception {
      
        AdministradorEntity admin = new AdministradorEntity(333L);
        administradorRepository.save(admin);

        PisoEntity piso = new PisoEntity("200","Carro");
        piso.setAdministrador(admin);
        pisoRepository.save(piso);

        // Añado otro piso para pruebas
        PisoEntity piso2 = new PisoEntity("300","Carro");
        piso2.setAdministrador(admin);
        pisoRepository.save(piso2);

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

        VehiculoEntity vehiculo = new VehiculoEntity("10:00 AM", "12:00 PM","ERE202","Carro");
        vehiculo.setPiso(piso);
        vehiculo.setTarifa(tarifaCarro);
        //piso.getVehiculos().add(vehiculo);
        vehiculoRepository.save(vehiculo);
        
    }
}
