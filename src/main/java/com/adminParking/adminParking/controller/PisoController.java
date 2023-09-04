package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;
import com.adminParking.adminParking.repositories.PisoRepository;
import java.util.List;

@RestController // @RestController
@RequestMapping("/pisos")
public class PisoController {

    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    AdministradorRepository administradorRepository;

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
        //Obtiene el administrador único
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null);
        
        if(administrador == null){
            System.out.println("Error encontrando el ID del administrador único");
            return null; 
        }

        piso.setAdministrador(administrador);

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
