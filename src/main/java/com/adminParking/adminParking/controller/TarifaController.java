package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TarifaRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/tarifas")
public class TarifaController {

    @Autowired
    private TarifaRepository tarifaRepository;

    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @Autowired
    PisoRepository pisoRepository;

    @Secured({ "ADMIN" })
    @GetMapping("/getTarifas")
    public List<TarifaEntity> getAllTarifas() {
        return tarifaRepository.findAll();
    }

    @GetMapping("/{id}")
    public TarifaEntity getTarifaById(@PathVariable Long id) {
        return tarifaRepository.findById(id).orElse(null);
    }
    /* 
    @GetMapping("/tipo/{tipoVehiculo}")
    public TarifaEntity getTarifaByTipo(@PathVariable String tipoVehiculo) {
        return tarifaRepository.findByTipoVehiculo(tipoVehiculo).orElse(null);
    }
    */
    /* 
    @PostMapping("/")
    public TarifaEntity createTarifa(@RequestBody TarifaEntity tarifa) {
        return tarifaRepository.save(tarifa);
    }*/

    @PostMapping("/")
    public String createTarifa(
        @RequestParam("tarifaPorMinuto") double tarifaPorMinutos,
        @RequestParam("tipoVehiculo") String tipoVehiculo,
        RedirectAttributes redirectAttributes) {

        // Verificar si el tipo de vehículo existe en la base de datos
        Optional<TipoVehiculoEntity> tipo = tipoVehiculoRepository.findByTipo(tipoVehiculo);

        if (tipo.isPresent()) {
            // Verificar si ya existe una tarifa para el tipo de vehículo
            TipoVehiculoEntity tipoV = tipo.get(); // Obtener la instancia de TipoVehiculoEntity del Optional

            Optional<TarifaEntity> existingTarifa = tarifaRepository.findByTipoVehiculo(tipoV);
            
            if (existingTarifa.isPresent()) {
                redirectAttributes.addFlashAttribute("errorTarifa", "Ya existe una tarifa para este tipo de vehículo");
            } else {
                // Crear una nueva tarifa solo si no existe
                TarifaEntity tarifa = new TarifaEntity(tipoV, tarifaPorMinutos);
                tarifaRepository.save(tarifa);
                redirectAttributes.addFlashAttribute("exitoTarifa", "Se guardó la tarifa correctamente.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorTarifa", "El tipo de vehículo no existe en la base de datos. No se puede crear la tarifa.");
        }


        return "redirect:/tarifas/anadirTarifa"; // Redirige de nuevo al formulario de creación
    }

    //Vista Para añadir una tarifa
    @GetMapping("/anadirTarifa")
    public String showMenu(Model model){

        if (model.containsAttribute("errorTarifa")) {
            model.addAttribute("mensaje", model.getAttribute("errorTarifa"));
        }else if(model.containsAttribute("exitoTarifa")){
            model.addAttribute("mensaje", model.getAttribute("exitoTarifa"));
        }
        return "crearTarifa";
    }

    @PutMapping("/{id}")
    public TarifaEntity updateTarifa(@PathVariable Long id, @RequestBody TarifaEntity tarifa) {
        tarifa.setId(id);
        return tarifaRepository.save(tarifa);
    }

    @DeleteMapping("/{id}")
    public void deleteTarifa(@PathVariable Long id) {
        tarifaRepository.deleteById(id);
    }

    
    @GetMapping("/calcular-espacios/{id}")
    public ResponseEntity<String> calcularEspaciosDisponibles(@PathVariable Long id) {

        int espaciosDisponibles = calcularEspaciosDisponiblesT(id);

        return ResponseEntity.ok("Espacios disponibles: " + espaciosDisponibles);
    }


    public int calcularEspaciosDisponiblesT(Long id) {
        
        int espaciosDisponibles = 0;
        PisoEntity pisoDef = pisoRepository.findById(id).orElse(null);

        if(pisoDef != null){
            int capacidadTotal = obtenerCapacidadTotalPorTipoDeVehiculo(pisoDef);

            // Obttiene la cantidad actual de vehículos estacionados en este piso
            int vehiculosEstacionados = pisoDef.getVehiculos().size();
            // Calcular los espacios disponibles restando los vehículos estacionados de la capacidad total
            espaciosDisponibles = capacidadTotal - vehiculosEstacionados;

            // resultado no sea negativo
            if (espaciosDisponibles < 0) {
                espaciosDisponibles = 0;
            }
        }

        return espaciosDisponibles;
    }

    private int obtenerCapacidadTotalPorTipoDeVehiculo(PisoEntity piso) {

        String tipoVehiculo = piso.getTipoVehiculo().getTipo();
        int areaPiso = Integer.parseInt(piso.getArea());

        int capacidadPorTipoYArea = obtenerCapacidadPorTipoYArea(tipoVehiculo);

        System.out.println("capacidadPorTipoYArea " + capacidadPorTipoYArea);
        System.out.println("areaPiso " + areaPiso);

        //Area por capacidad por tipo de vehiculo
        return areaPiso * capacidadPorTipoYArea;

    }

    private int obtenerCapacidadPorTipoYArea(String tipoVehiculo){
        Map<String, Integer> capacidadPorTipoYArea = new HashMap<>();
        capacidadPorTipoYArea.put("Carro", 2); // 2 automóviles por metro cuadrado
        capacidadPorTipoYArea.put("Moto", 3); // 3 metros por metro cuadrado
        capacidadPorTipoYArea.put("Camión", 1);   // 1 camión por metro cuadrado
        capacidadPorTipoYArea.put("Scooter", 4);   // 5 Scooters por metro cuadrado

        // Obtener la capacidad para el tipo de vehículo dado
        Integer capacidad = capacidadPorTipoYArea.get(tipoVehiculo);

        // Verificar si se encontró una capacidad para el tipo de vehículo
        if (capacidad != null) {
            return capacidad;
        } else {
        // Aquí devolvemos -1 como valor predeterminado
        return -1;
        }

    }
    
}
