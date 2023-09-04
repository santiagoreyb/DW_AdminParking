package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.repositories.TarifaRepository;

import java.util.List;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    @Autowired
    private TarifaRepository tarifaRepository;

    @GetMapping("/")
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

    @PostMapping("/")
    public TarifaEntity createTarifa(@RequestBody TarifaEntity tarifa) {
        return tarifaRepository.save(tarifa);
    }

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
