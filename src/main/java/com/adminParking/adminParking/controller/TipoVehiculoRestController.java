package com.adminParking.adminParking.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

@RestController
@RequestMapping("/tiposvehiculoRest")
public class TipoVehiculoRestController {
    

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository;

    @Autowired
    PisoRepository pisoRepository;

    @Secured({ "ADMIN", "PORTERO" })
    @GetMapping("/getTipos")
    public List<TipoVehiculoEntity> getAllTarifas() {
        return tipoVehiculoRepository.findAll();
    }

    @Secured({ "ADMIN", "PORTERO" })
    @GetMapping("/{id}")
    public TipoVehiculoEntity getTipoById(@PathVariable Long id) {
        return tipoVehiculoRepository.findById(id).orElse(null);
    }

    @Secured({ "ADMIN"})
    @PostMapping("/anadirTipoVehiculo")
    public void createVehiculo(@RequestBody String tipo) {
        TipoVehiculoEntity tipoV = new TipoVehiculoEntity(tipo);
        System.out.println("Received tipo: " + tipo);
        tipoVehiculoRepository.save(tipoV);
    }

    @Secured({ "ADMIN" })
    @DeleteMapping("/deleteTipoVehiculo/{id}")
    public void deleteTipo(@PathVariable Long id) {
        System.out.println("Received id: " + id);
        tipoVehiculoRepository.deleteById(id);
    }

    @GetMapping("/getPisosByTipoVehiculo/{id}")
    @Secured({ "ADMIN"})
    public List<PisoEntity> getAllPisosByTipoVehiculo(@PathVariable Long id) {
        TipoVehiculoEntity tipoVehiculo = tipoVehiculoRepository.findById(id).orElse(null);
        List<PisoEntity> pisosConTipo = pisoRepository.findByTipoVehiculo(tipoVehiculo);
        return pisosConTipo;
    }


}