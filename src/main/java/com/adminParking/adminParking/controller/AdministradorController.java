package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adminParking.adminParking.model.AdministradorEntity;
import com.adminParking.adminParking.repositories.AdministradorRepository;


@RestController
@RequestMapping("/admin")
public class AdministradorController {

    @Autowired
    AdministradorRepository administradorRepository; // Debes inyectar tu repositorio de administradores aquí

    @GetMapping("/calcular-espacios/{id}")
    public ResponseEntity<String> calcularEspaciosDisponibles(@PathVariable Long id) {
        // Supongamos que tienes una instancia válida de AdministradorEntity
        AdministradorEntity administrador = administradorRepository.findById(333L).orElse(null); 
        if (administrador == null) {
            return ResponseEntity.notFound().build();
        }

        int espaciosDisponibles = administrador.calcularEspaciosDisponibles(id);

        return ResponseEntity.ok("Espacios disponibles: " + espaciosDisponibles);
    }


}
