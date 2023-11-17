package com.adminParking.adminParking.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.Role;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.User;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.UserRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@Component
@Profile({"default"})
public class DBInitializer implements ApplicationRunner {

    @Autowired
    private PisoRepository pisoRepository;

    @Autowired
    private TipoVehiculoRepository tipoVehiculoRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TarifaRepository tarifaRepository; 
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        TipoVehiculoEntity tipo = new TipoVehiculoEntity("Carro");
        TipoVehiculoEntity tipo2 = new TipoVehiculoEntity("Moto");
        tipoVehiculoRepository.save(tipo);
        tipoVehiculoRepository.save(tipo2);
        PisoEntity piso = new PisoEntity("200",tipo);
        piso.setCapacidad(200/2);
        pisoRepository.save(piso);

        // Añado otro piso para pruebas
        PisoEntity piso2 = new PisoEntity("300",tipo2);
        piso2.setCapacidad(300/2);
        pisoRepository.save(piso2);

        // Inicializar tarifas
        TarifaEntity tarifaCarro = new TarifaEntity();
        tarifaCarro.setTipoVehiculo(tipo);
        tarifaCarro.setTarifaPorMinuto(900); // Establece la tarifa por minuto para carros

        TarifaEntity tarifaMoto = new TarifaEntity();
        tarifaMoto.setTipoVehiculo(tipo2);
        tarifaMoto.setTarifaPorMinuto(300); // Establece la tarifa por minuto para motos

        // Guardar las tarifas en la base de datos
        tarifaRepository.save(tarifaCarro);
        tarifaRepository.save(tarifaMoto);

        VehiculoEntity vehiculo = new VehiculoEntity("10:00", "19:00","ERE202",tipo);
        vehiculo.setPiso(piso);
        vehiculo.setTarifa(tarifaCarro);
        //piso.getVehiculos().add(vehiculo);
        vehiculoRepository.save(vehiculo);

        userRepository.save(
                new User("Alice", "Alisson", "alice@alice.com", passwordEncoder.encode("alice123"), Role.ADMIN));
        userRepository.save(
                new User("Bob", "Bobson", "bob@bob.com", passwordEncoder.encode("bob123"), Role.CONDUCTOR));
        
    }
}
