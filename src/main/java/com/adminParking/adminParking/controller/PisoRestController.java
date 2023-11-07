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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pisosRest")
public class PisoRestController {

    @Autowired
    PisoRepository pisoRepository;


    @Autowired 
    VehiculoRepository vehiculoRepository;

    @GetMapping("/getPisos")
    public List<PisoEntity> getAllTarifas() {
        return pisoRepository.findAll();
    }
    @GetMapping("/{id}")
    public PisoEntity getPisoById(@PathVariable Long id) {
        return pisoRepository.findById(id).orElse(null);
    }

    @PostMapping("/")
    public PisoEntity createPiso(@RequestBody PisoEntity piso) {
        //Obtiene el administrador único
        /*AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null);
        
        if(administrador == null){
            System.out.println("Error encontrando el ID del administrador único");
            return null; 
        }

        piso.setAdministrador(administrador);*/

        return pisoRepository.save(piso);
    }

    @PostMapping("/updateEspacios")
    public void updateEspacios(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()-1);
        pisoRepository.save(piso);
    }

    @PostMapping("/salirVehiculoPiso")
    public void salirVehiculoPiso(@RequestBody Long id) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        piso.setCapacidad(piso.getCapacidad()+1);
        pisoRepository.save(piso);

    }

    @PostMapping("/sacarVehiculoPiso")
    public void sacarVehiculoPiso(@RequestBody Long id) {
        VehiculoEntity vehiculo = vehiculoRepository.findById(id).orElse(null);
        vehiculo.setTiempoSalida(obtenerFechaYHoraActual());
        vehiculo.setPiso(null);
        vehiculoRepository.save(vehiculo);
    }

    public String obtenerFechaYHoraActual() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date now = new Date();
        return formato.format(now);
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
