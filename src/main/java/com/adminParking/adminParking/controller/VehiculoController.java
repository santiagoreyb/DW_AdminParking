package com.adminParking.adminParking.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    PisoRepository pisoRepository;

    @Autowired 
    TarifaRepository tarifaRepository; 


    @Secured({ "ADMIN" })
    @GetMapping("/verVehiculos")
    public List<VehiculoEntity> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    
    
    @Secured({ "ADMIN" })
    @GetMapping("/{id}")
    public VehiculoEntity getVehiculoById(@PathVariable Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    @Secured({ "ADMIN" })
    @PostMapping("")
    public void createVehiculo(@RequestBody VehiculoEntity vehiculo) {
        vehiculoRepository.save(vehiculo);
    }
    

    @Secured({ "ADMIN" })
    @PutMapping("/{id}")
    public VehiculoEntity updateVehiculo(@PathVariable Long id, @RequestBody VehiculoEntity vehiculo) {
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }

    @Secured({ "ADMIN" })
    @DeleteMapping("/eliminarVehiculo/{id}")
    public void deleteVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }

    @Secured({ "ADMIN" })
    @GetMapping("/anadirVehiculo")
    public String showMenu(Model model){
        // Verificar si hay mensajes de error en el modelo
        if (model.containsAttribute("errorPiso")) {
            model.addAttribute("mensajeError", model.getAttribute("errorPiso"));
        } else if (model.containsAttribute("errorTarifa")) {
            model.addAttribute("mensajeError", model.getAttribute("errorTarifa"));
        }else if (model.containsAttribute("exito")){
            model.addAttribute("mensajeExito", model.getAttribute("exito"));
        }
        return "anadirVehiculo";
    }

    @Secured({ "ADMIN" })
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

}
