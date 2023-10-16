package com.adminParking.adminParking.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.repositories.TarifaRepository;

@RestController
@RequestMapping("/tarifasRest")
public class TarifaRestController {
    
    @Autowired
    private TarifaRepository tarifaRepository;


    @CrossOrigin(origins =  "http://localhost:4200")
    @GetMapping("/getTarifas")
    public List<TarifaEntity> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    @GetMapping("/{id}")
    public TarifaEntity getTarifaById(@PathVariable Long id) {
        return tarifaRepository.findById(id).orElse(null);
    }

    @GetMapping("/tipo/{tipoVehiculo}")
    public TarifaEntity getTarifaByTipo(@PathVariable String tipoVehiculo) {
        return tarifaRepository.findByTipoVehiculo(tipoVehiculo).orElse(null);
    }

    /* 
    @PostMapping("/")
    public TarifaEntity createTarifa(@RequestBody TarifaEntity tarifa) {
        return tarifaRepository.save(tarifa);
    }*/

    @PutMapping("/{id}")
    public TarifaEntity updateTarifa(@PathVariable Long id, @RequestBody TarifaEntity tarifa) {
        tarifa.setId(id);
        return tarifaRepository.save(tarifa);
    }

    @DeleteMapping("/{id}")
    public void deleteTarifa(@PathVariable Long id) {
        tarifaRepository.deleteById(id);
    }

}
