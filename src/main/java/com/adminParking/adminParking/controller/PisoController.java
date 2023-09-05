package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;
import com.adminParking.adminParking.repositories.PisoRepository;
import java.util.List;

@Controller // @RestController
@RequestMapping("/pisos")
public class PisoController {

    @Autowired
    PisoRepository pisoRepository;

    @Autowired
    AdministradorRepository administradorRepository;

    @GetMapping("/recu")
    public String getAllPisos ( Model model ) {
        List<PisoEntity> pisos = pisoRepository.findAll();
        model.addAttribute("pisos", pisos);
        return "recuperar pisos";
    }

    @GetMapping("/{id}")
    public PisoEntity getPisoById(@PathVariable Long id) {
        return pisoRepository.findById(id).orElse(null);
    }

    /* 

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
    }*/

    @PostMapping("/")
    public String createPiso(@RequestParam("area") String area, @RequestParam("tipoVehiculo") String tipoVehiculo){

        PisoEntity piso = new PisoEntity(area, tipoVehiculo);
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null);

        if(administrador == null){
            System.out.println("Error encontrando el ID del administrador único");
            return null; 
        }
        piso.setAdministrador(administrador);
        pisoRepository.save(piso);

        return "redirect:/pisos/recu";
    }

    //Vista Para añadir un piso
    @GetMapping("/anadirPiso")
    public String showMenu(Model model){
        return "crearPiso"; 
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
