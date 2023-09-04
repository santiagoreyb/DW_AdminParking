package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import java.util.List;

@RestController // @RestController
@RequestMapping("/pisos")
public class PisoController {

    @Autowired
    PisoRepository pisoRepository;

    @GetMapping("/")
    public List<PisoEntity> getAllPisos() {
        return pisoRepository.findAll();
    }

    @GetMapping("/{id}")
    public PisoEntity getPisoById(@PathVariable Long id) {
        return pisoRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public PisoEntity createPiso(@RequestBody PisoEntity piso) {
        return pisoRepository.save(piso);
    }

    @PutMapping("/{id}")
    public PisoEntity updatePiso(@PathVariable Long id, @RequestBody PisoEntity piso) {
        piso.setId(id);
        return pisoRepository.save(piso);
    }

    @DeleteMapping("/{id}")
    public void deletePiso(@PathVariable Long id) {
        pisoRepository.deleteById(id);
    }
}
