package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

import jakarta.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    @CrossOrigin(origins =  "http://localhost:4200")
    @GetMapping("/getPisos")
    public List<PisoEntity> getAllTarifas() {
        return pisoRepository.findAll();
    }

    @CrossOrigin(origins =  "http://localhost:4200")
    @GetMapping("/{id}")
    public PisoEntity getPisoById(@PathVariable Long id) {
        return pisoRepository.findById(id).orElse(null);
    }

    @CrossOrigin(origins =  "http://localhost:4200")
    @PostMapping("/")
    public PisoEntity createPiso(@RequestBody PisoEntity piso) {
        piso.setCapacidad(obtenerCapacidadTotalPorTipoDeVehiculo(piso));
        return pisoRepository.save(piso);
    }

    @CrossOrigin(origins =  "http://localhost:4200")
    @PostMapping("/updateEspacios")
    @Transactional
    public void updateEspacios(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()-1);
        pisoRepository.save(piso);
    }

    @CrossOrigin(origins =  "http://localhost:4200")
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

            // Obttiene la cantidad actual de vehículos estacionados en este piso
            int vehiculosEstacionados = pisoDef.getVehiculos().size();

        // System.out.println(vehiculosEstacionados);

            // Calcular los espacios disponibles restando los vehículos estacionados de la capacidad total
            espaciosDisponibles = capacidadTotal - vehiculosEstacionados;

            // resultado no sea negativo
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

    
    /*
    @PutMapping("/{id}")
    public PisoEntity updatePiso(@PathVariable Long id, @RequestBody PisoEntity piso) {
        piso.setId(id);
        return pisoRepository.save(piso);
    }
    */

    /* 
    @DeleteMapping("/{id}")
    public void deletePiso(@PathVariable Long id) {
        pisoRepository.deleteById(id);
    }*/

