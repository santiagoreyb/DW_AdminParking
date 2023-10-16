package com.adminParking.adminParking.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;

//@RestController
@Controller
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    AdministradorRepository administradorRepository; //repositorio de administradores aquí

    @Autowired 
    PisoRepository pisoRepository;

    @Autowired
    TarifaRepository tarifaRepository;

   /*  @GetMapping("/calcular-espacios/{id}")
    public ResponseEntity<String> calcularEspaciosDisponibles(@PathVariable Long id) {
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null); 
        if (administrador == null) {
            return ResponseEntity.notFound().build();
        }

        int espaciosDisponibles = administrador.calcularEspaciosDisponibles(id);

        return ResponseEntity.ok("Espacios disponibles: " + espaciosDisponibles);
    }*/


    @GetMapping("/calcular-espacios/{id}")
    public ResponseEntity<String> calcularEspaciosDisponibles(@PathVariable Long id) {
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null); 
        if (administrador == null) {
            return ResponseEntity.notFound().build();
        }

        int espaciosDisponibles = administrador.calcularEspaciosDisponibles(id);

        return ResponseEntity.ok("Espacios disponibles: " + espaciosDisponibles);
    }

    @GetMapping("tarifas-espacios")
    public String mostrarTarifas_Espacios(Model model){

        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null); 
        if (administrador == null) {
            return "No exitse un administrador";
        }

        List<PisoEntity> pisos = pisoRepository.findAll();

        List<String> infoEspaciosPorPiso = new ArrayList<>();

        for (PisoEntity piso : pisos) {
            String infoPiso = "Piso ID: " + piso.getId() + ", Tipo de Vehículo: " + piso.getTipoVehiculo() + ", Espacios Disponibles: " + piso.getCapacidad();
            infoEspaciosPorPiso.add(infoPiso);
        }

        List<TarifaEntity> tarifas = tarifaRepository.findAll();
        model.addAttribute("tarifas", tarifas);
        model.addAttribute("infoEspaciosPorPiso", infoEspaciosPorPiso);
        return "tarifas-espacios";
    }

}