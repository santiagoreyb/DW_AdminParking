package com.adminParking.adminParking.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/verVehiculos")
    public List<VehiculoEntity> getAllVehiculos() {
        return vehiculoRepository.findAll();
    }

    
    
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public VehiculoEntity getVehiculoById(@PathVariable Long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    //Rest
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("")
    public void createVehiculo(@RequestBody VehiculoEntity vehiculo) {
        vehiculoRepository.save(vehiculo);
    }
    

    //Rest
    // @CrossOrigin(origins="http://localhost:4200")
    // @PostMapping("")
    // public VehiculoEntity createVehiculo(@RequestBody VehiculoEntity vehiculo) {
    //     System.out.println(vehiculo.getPlaca());
    //     System.out.println("jolasas");
    //     Logger log = LogManager.getLogger(vehiculo);
    //     log.info(vehiculo.getPlaca());
    //     // Obtiene el ID del piso desde la solicitud 
    //     Long pisoId = vehiculo.getPiso().getId();
    //     // Obtiene el piso correspondiente desde la base de datos
    //     PisoEntity piso = pisoRepository.findById(pisoId).orElse(null);
    //     // Asigna el piso al vehículo
    
    //     if (piso != null && piso.getTipoVehiculo().equals(vehiculo.getTipoVehiculo())) {
    //         // Busca la tarifa automáticamente por el tipo de vehículo
    //         TarifaEntity tarifa = tarifaRepository.findByTipoVehiculo(vehiculo.getTipoVehiculo()).orElse(null);
            
    //         if (tarifa != null) {
    //             // Asigna el piso al vehículo   
    //             vehiculo.setPiso(piso);
    //             // Asigna la tarifa automáticamente
    //             vehiculo.setTarifa(tarifa);
    //             // Guarda el vehículo en la base de datos
    //             System.out.println("El vehiculo se guardo correctamente");
    //             return vehiculoRepository.save(vehiculo);
    //         } else {
    //             System.out.println("No se encontró una tarifa para este tipo de vehículo");
    //             return null;
    //         }
    //     } else {
    //         System.out.println("El vehiculo no se puede guardar en este piso");
    //         return null;
    //     }
    // }

    /* 
    @PostMapping("/crearVehiculo")
    public String createVehiculo(
            @RequestParam("tipoVehiculo") String tipoVehiculo,
            @RequestParam("placa") String placa,
            @RequestParam("TiempoLlegada") String tiempoLlegada,
            @RequestParam("TiempoSalida") String tiempoSalida,
            @RequestParam("IdPiso") Long idPiso,
            RedirectAttributes redirectAttributes) { // Cambiar de Model a RedirectAttributes

        // Crear una instancia de VehiculoEntity
        VehiculoEntity vehiculo = new VehiculoEntity(tiempoLlegada, tiempoSalida, placa, tipoVehiculo);

        // Obtener el piso correspondiente desde la base de datos
        PisoEntity piso = pisoRepository.findById(idPiso).orElse(null);

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
                System.out.println("Vehiculo registrado con exito");
                redirectAttributes.addFlashAttribute("exito", "El vehiculo se ha guardado con éxito");
            } else {
                // No se encontró una tarifa para este tipo de vehículo
                // Agregar mensaje de error para mostrar en la vista anadirVehiculo
                redirectAttributes.addFlashAttribute("errorTarifa", "No se encontró una tarifa para este tipo de vehículo");
            }
        } else {
            // El vehículo no se puede guardar en este piso
            // Agregar mensaje de error para mostrar en la vista anadirVehiculo
            System.out.println("El vehiculo no se puede guardar en este piso");
            redirectAttributes.addFlashAttribute("errorPiso", "El vehiculo no se puede guardar en este piso");
        }

        return "redirect:/vehiculos/anadirVehiculo"; // Redirige después de agregar los mensajes de error
    }*/

    @CrossOrigin(origins =  "http://localhost:4200")
    @PutMapping("/{id}")
    public VehiculoEntity updateVehiculo(@PathVariable Long id, @RequestBody VehiculoEntity vehiculo) {
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }

    @CrossOrigin(origins =  "http://localhost:4200")
    @DeleteMapping("/eliminarVehiculo/{id}")
    public void deleteVehiculo(@PathVariable Long id) {
        vehiculoRepository.deleteById(id);
    }

    @CrossOrigin(origins =  "http://localhost:4200")
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

    @CrossOrigin(origins =  "http://localhost:4200")
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
