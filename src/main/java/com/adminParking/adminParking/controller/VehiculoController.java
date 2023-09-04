package com.adminParking.adminParking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    PisoRepository pisoRepository;

    @Autowired 
    TarifaRepository tarifaRepository; 

    @GetMapping("/")
    public List<VehiculoEntity> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    @GetMapping("/{id}")
    public VehiculoEntity getVehiculoById(@PathVariable Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public VehiculoEntity createVehiculo(@RequestBody VehiculoEntity vehiculo) {
        // Obtiene el ID del piso desde la solicitud 
        Long pisoId = vehiculo.getPiso().getId();
        // Obtiene el piso correspondiente desde la base de datos
        PisoEntity piso = pisoRepository.findById(pisoId).orElse(null);
        // Asigna el piso al vehículo
    
        if (piso != null && piso.getTipoVehiculo().equals(vehiculo.getTipoVehiculo())) {
            // Busca la tarifa automáticamente por el tipo de vehículo
            TarifaEntity tarifa = tarifaRepository.findByTipoVehiculo(vehiculo.getTipoVehiculo()).orElse(null);
            
            if (tarifa != null) {
                // Asigna el piso al vehículo   
                vehiculo.setPiso(piso);
                // Asigna la tarifa automáticamente
                vehiculo.setTarifa(tarifa);
                // Guarda el vehículo en la base de datos
                System.out.println("El vehiculo se guardo correctamente");
                return vehiculoRepository.save(vehiculo);
            } else {
                System.out.println("No se encontró una tarifa para este tipo de vehículo");
                return null;
            }
        } else {
            System.out.println("El vehiculo no se puede guardar en este piso");
            return null;
        }
    }
    

    @PutMapping("/{id}")
    public VehiculoEntity updateVehiculo(@PathVariable Long id, @RequestBody VehiculoEntity vehiculo) {
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }

    @DeleteMapping("/{id}")
    public void deleteVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }
}
