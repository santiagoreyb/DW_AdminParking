package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createPiso(@RequestParam("area") String area, @RequestParam("tipoVehiculo") String tipoVehiculo, RedirectAttributes redirectAttributes){

        PisoEntity piso = new PisoEntity(area, tipoVehiculo);
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null);

        if(administrador == null){
            System.out.println("Error encontrando el ID del administrador único");
            redirectAttributes.addFlashAttribute("error", "Error al añadir el piso. No se encontró el administrador.");
        }{
            piso.setAdministrador(administrador);
            pisoRepository.save(piso);
            redirectAttributes.addFlashAttribute("exito", "Piso añadido exitosamente.");
        }

        return "redirect:/pisos/anadirPiso";
    }

    //Vista Para añadir un piso
    @GetMapping("/anadirPiso")
    public String showMenu(Model model){
        return "crearPiso";
    }

    @PutMapping("/actu")
    public PisoEntity updatePiso(@RequestParam("id") Long id, @RequestParam("area") String area , @RequestParam("tipoVehiculo") String tipoVehiculo, RedirectAttributes redirectAttributes) {

        PisoEntity piso = pisoRepository.findById(id).orElse(null) ;

        if ( piso != null ) {
            piso.setArea(area);
            piso.setTipoVehiculo(tipoVehiculo);
            redirectAttributes.addFlashAttribute("exito", "Piso actualizado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al encontrar el piso.");
        }

        return pisoRepository.save(piso);
    }

    /*
    @PutMapping("/{id}")
    public PisoEntity updatePiso(@PathVariable Long id, @RequestBody PisoEntity piso) {
        piso.setId(id);
        return pisoRepository.save(piso);
    }
    */

    @DeleteMapping("/{id}")
    public void deletePiso(@PathVariable Long id) {
        pisoRepository.deleteById(id);
    }

}
