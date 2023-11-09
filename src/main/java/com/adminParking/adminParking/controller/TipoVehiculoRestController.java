package com.adminParking.adminParking.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

@RestController
@RequestMapping("/tiposvehiculoRest")
public class TipoVehiculoRestController {
    
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @CrossOrigin(origins =  "http://localhost:4200")
    @GetMapping("/getTipos")
    public List<TipoVehiculoEntity> getAllTarifas() {
        return tipoVehiculoRepository.findAll();
    }

    @CrossOrigin(origins =  "http://localhost:4200")
    @GetMapping("/{id}")
    public TipoVehiculoEntity getTipoById(@PathVariable Long id) {
        return tipoVehiculoRepository.findById(id).orElse(null);
    }

}
