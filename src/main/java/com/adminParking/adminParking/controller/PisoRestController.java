package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pisosRest")
public class PisoRestController {

    @Autowired
    PisoRepository pisoRepository;


    @Autowired 
    VehiculoRepository vehiculoRepository;
    @GetMapping("/getPisos")
    @Secured({ "ADMIN" })
    public List<PisoEntity> getAllTarifas() {
        return pisoRepository.findAll();
    }

    @Secured({ "ADMIN" })
    @GetMapping("/{id}")
    public PisoEntity getPisoById(@PathVariable Long id) {
        return pisoRepository.findById(id).orElse(null);
    }

    @Secured({ "ADMIN" })
    @PostMapping("/")
    public PisoEntity createPiso(@RequestBody PisoEntity piso) {
        piso.setCapacidad(obtenerCapacidadTotalPorTipoDeVehiculo(piso));
        return pisoRepository.save(piso);
    }

    @Secured({ "ADMIN" })
    @PostMapping("/updateEspacios")
    public void updateEspacios(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()-1);
        pisoRepository.save(piso);
    }

    @Secured({ "ADMIN" })
    @PostMapping("/salirVehiculoPiso")
    public void salirVehiculoPiso(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()+1);
        pisoRepository.save(piso);

    }

    public int calcularEspaciosDisponibles(Long id) {
        
        int espaciosDisponibles = 0;
        PisoEntity pisoDef = pisoRepository.findById(id).orElse(null);

        if(pisoDef != null){
            int capacidadTotal = obtenerCapacidadTotalPorTipoDeVehiculo(pisoDef);

            int vehiculosEstacionados = pisoDef.getVehiculos().size();
            espaciosDisponibles = capacidadTotal - vehiculosEstacionados;

            if (espaciosDisponibles < 0) {
                espaciosDisponibles = 0;
            }
        }

        return espaciosDisponibles;
    }

    private int obtenerCapacidadTotalPorTipoDeVehiculo(PisoEntity piso) {

        String tipoVehiculo = piso.getTipoVehiculo().getTipo();
        int areaPiso = Integer.parseInt(piso.getArea());

        int capacidadPorTipoYArea = obtenerCapacidadPorTipoYArea(tipoVehiculo);

        System.out.println("capacidadPorTipoYArea " + capacidadPorTipoYArea);
        System.out.println("areaPiso " + areaPiso);

        //Area por capacidad por tipo de vehiculo
        return areaPiso * capacidadPorTipoYArea;

    }

    private int obtenerCapacidadPorTipoYArea(String tipoVehiculo){
        Map<String, Integer> capacidadPorTipoYArea = new HashMap<>();
        capacidadPorTipoYArea.put("Carro", 2); // 2 automóviles por metro cuadrado
        capacidadPorTipoYArea.put("Moto", 3); // 3 metros por metro cuadrado
        capacidadPorTipoYArea.put("Camión", 1);   // 1 camión por metro cuadrado
        capacidadPorTipoYArea.put("Scooter", 4);   // 5 Scooters por metro cuadrado

        // Obtener la capacidad para el tipo de vehículo dado
        Integer capacidad = capacidadPorTipoYArea.get(tipoVehiculo);

        // Verificar si se encontró una capacidad para el tipo de vehículo
        if (capacidad != null) {
            return capacidad;
        } else {
        // Aquí devolvemos -1 como valor predeterminado
        return -1;
        }
    }
    
}

