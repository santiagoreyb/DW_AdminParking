package com.adminParking.adminParking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

//@RestController
@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {
    @Autowired
    VehiculoRepository vehiculoRepository;

    @Autowired
    PisoRepository pisoRepository;

    @Autowired 
    TarifaRepository tarifaRepository; 

    @GetMapping("/verVehiculos")
    public List<VehiculoEntity> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    @GetMapping("/{id}")
    public VehiculoEntity getVehiculoById(@PathVariable Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }
    

    //Rest
    /* 
    @PostMapping("/")
    public VehiculoEntity createVehiculo(@RequestBody VehiculoEntity vehiculo) {
        // Obtiene el ID del piso desde la solicitud 
        Long pisoId = vehiculo.getPiso().getId();
        // Obtiene el piso correspondiente desde la base de datos
        PisoEntity piso = pisoRepository.findById(pisoId).orElse(null);
        // Asigna el piso al vehículo
    
        if (piso != null && piso.getTipoVehiculo().equals(vehiculo.getTipoVehiculo())) {
            // Busca la tarifa automáticamente por el tipo de vehículo
            TarifaEntity tarifa = tarifaRepository.findByTipoVehiculo(vehiculo.getTipoVehiculo()).orElse(null);
            
            if (tarifa != null) {
                // Asigna el piso al vehículo   
                vehiculo.setPiso(piso);
                // Asigna la tarifa automáticamente
                vehiculo.setTarifa(tarifa);
                // Guarda el vehículo en la base de datos
                System.out.println("El vehiculo se guardo correctamente");
                return vehiculoRepository.save(vehiculo);
            } else {
                System.out.println("No se encontró una tarifa para este tipo de vehículo");
                return null;
            }
        } else {
            System.out.println("El vehiculo no se puede guardar en este piso");
            return null;
        }
    }*/

    @PostMapping("/crearVehiculo")
    public String createVehiculo(
            @RequestParam("tipoVehiculo") String tipoVehiculo,
            @RequestParam("placa") String placa,
            @RequestParam("TiempoLlegada") String tiempoLlegada,
            @RequestParam("TiempoSalida") String tiempoSalida,
            @RequestParam("IdPiso") Long idPiso) {
    
        // Crear una instancia de VehiculoEntity
        VehiculoEntity vehiculo = new VehiculoEntity(tiempoLlegada, tiempoSalida, placa, tipoVehiculo);
    
        // Obtener el piso correspondiente desde la base de datos
        //PisoEntity piso = pisoRepository.findById(idPiso).orElse(null);

        PisoEntity piso = pisoRepository.findById(1L).orElse(null);

        System.out.println("esadasd" + pisoRepository.findById(idPiso));

        System.out.println("tiempo llegada " + tiempoLlegada);
        System.out.println("tiempo salida " + tiempoSalida);
        System.out.println("placa " + placa);
        System.out.println("tipo vehiculo " + tipoVehiculo);


        // Asignar el piso al vehículo si es válido
        if (piso != null && piso.getTipoVehiculo().equals(vehiculo.getTipoVehiculo())) {
            // Buscar la tarifa automáticamente por el tipo de vehículo
            TarifaEntity tarifa = tarifaRepository.findByTipoVehiculo(vehiculo.getTipoVehiculo()).orElse(null);
    
            if (tarifa != null) {
                // Asignar el piso al vehículo
                vehiculo.setPiso(piso);
                // Asignar la tarifa automáticamente
                vehiculo.setTarifa(tarifa);
                // Guardar el vehículo en la base de datos
                vehiculoRepository.save(vehiculo);
    
                // Redirigir a una página de éxito después de guardar el vehículo
                return "redirect:/vehiculos/exito"; // Cambia "/vehiculos/exito" por la URL real de la página de éxito
            } else {
                // No se encontró una tarifa para este tipo de vehículo
                // Redirigir a una página de error (o mostrar un mensaje de error)
                return "redirect:/vehiculos/error-tarifa"; // Cambia "/vehiculos/error-tarifa" por la URL real de la página de error
            }
        } else {
            // El vehículo no se puede guardar en este piso
            // Redirigir a una página de error (o mostrar un mensaje de error)
            return "redirect:/vehiculos/error-piso"; // Cambia "/vehiculos/error-piso" por la URL real de la página de error
        }
    }
    

    @PutMapping("/{id}")
    public VehiculoEntity updateVehiculo(@PathVariable Long id, @RequestBody VehiculoEntity vehiculo) {
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }

    @DeleteMapping("/{id}")
    public void deleteVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }


    //Vista Para añadir un vehiculo
    @GetMapping("/anadirVehiculo")
    public String showMenu(Model model){
        return "anadirVehiculo"; 
    }

}
