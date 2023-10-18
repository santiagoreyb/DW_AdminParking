package com.adminParking.adminParking.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;

@Controller
@RequestMapping ( "/tiposvehiculo" )
public class TipoVehiculoController {


    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @Autowired
    PisoRepository pisoRepository;

    @GetMapping ("/verTipos")
    public List<TipoVehiculoEntity> getAllTipos ( ) {
        return tipoVehiculoRepository.findAll();
    }

    
    @GetMapping("/{id}")
    public TipoVehiculoEntity getTipoVehiculoById(@PathVariable Long id) {
        return tipoVehiculoRepository.findById(id).orElse(null);
    }

    /* 
    @PostMapping("/")
    public TipoVehiculoEntity createTipoVehiculo(@RequestBody TipoVehiculoEntity tipo) {
        return tipoVehiculoRepository.save(tipo);
    }*/
    @PostMapping("/")
    public String createTipoVehiculo(
        @RequestParam("tipoVehiculo") String tipo,
        RedirectAttributes redirectAttributes) {
        
        // Verificar si ya existe un tipo para el tipo de vehículo
        Optional<TipoVehiculoEntity> existingTarifa = tipoVehiculoRepository.findByTipo(tipo);
    
        if (existingTarifa.isPresent()) {
            redirectAttributes.addFlashAttribute("errorTipo", "Ya existe este tipo de vehículo");
        } else {
            // Crear una nuevo tipo solo si no existe
            TipoVehiculoEntity tipoV = new TipoVehiculoEntity(tipo);
            tipoVehiculoRepository.save(tipoV);
            redirectAttributes.addFlashAttribute("exitoTipo", "Se guardo el tipo correctamente");
        }
    
        return "redirect:/tiposvehiculo/anadirTipo"; // Redirige de nuevo al formulario de creación
    }
    
    @PostMapping("/deleteTipo")
    public String deleteTipo(@RequestParam("tipo") Long tipoId, RedirectAttributes redirectAttributes) {
        TipoVehiculoEntity tipoVehiculo = tipoVehiculoRepository.findById(tipoId).orElse(null);
        
        if (tipoVehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "Tipo de vehículo no encontrado");
        } else {
            List<PisoEntity> pisosConTipo = pisoRepository.findByTipoVehiculo(tipoVehiculo);
            
            if (!pisosConTipo.isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "Existen pisos con este tipo de vehículo. Elimine los pisos primero.");
            } else {
                tipoVehiculoRepository.deleteById(tipoId);
                redirectAttributes.addFlashAttribute("exito", "Tipo de vehículo eliminado correctamente");
            }
        }
        
        return "redirect:/tiposvehiculo/borrar";
    }

    // Vista para actualizar un tipo.
    @GetMapping("/borrar")
    public String borrarTipo(Model model) {
        List<TipoVehiculoEntity> tipos = tipoVehiculoRepository.findAll();
        model.addAttribute("tipos", tipos); // Agrega la lista de tipos al modelo
        return "eliminarTipo";
    }

    //Vista Para añadir una tarifa
    @GetMapping("/anadirTipo")
    public String showMenu(Model model){

        if (model.containsAttribute("errorTipo")) {
            model.addAttribute("mensaje", model.getAttribute("errorTipo"));
        }else if(model.containsAttribute("exitoTipo")){
            model.addAttribute("mensaje", model.getAttribute("exitoTipo"));
        }
        return "crearTipoVehiculo";
    }

    @PutMapping("/{id}")
    public TipoVehiculoEntity updateTipo(@PathVariable Long id, @RequestBody TipoVehiculoEntity tipo) {
        tipo.setId(id);
        return tipoVehiculoRepository.save(tipo);
    }

    @DeleteMapping("/{id}")
    public void deleteTipo(@PathVariable Long id) {
        tipoVehiculoRepository.deleteById(id);
    }

}