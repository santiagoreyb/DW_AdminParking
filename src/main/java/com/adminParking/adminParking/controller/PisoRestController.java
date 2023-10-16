package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

import java.util.List;

@RestController
@RequestMapping("/pisosRest")
public class PisoRestController {

    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    AdministradorRepository administradorRepository;

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

    @CrossOrigin(origins =  "http://localhost:4200")
    @PostMapping("/updateEspacios")
    public void updateEspacios(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()-1);
        pisoRepository.save(piso);
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


    
}
