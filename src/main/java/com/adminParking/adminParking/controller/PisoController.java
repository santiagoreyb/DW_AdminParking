package com.adminParking.adminParking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;
import com.adminParking.adminParking.model.VehiculoEntity;
import com.adminParking.adminParking.repositories.PisoRepository;
import com.adminParking.adminParking.repositories.TipoVehiculoRepository;
import com.adminParking.adminParking.repositories.VehiculoRepository;

import java.util.List;

@Controller // @RestController
@RequestMapping("/pisos")
public class PisoController {

    @Autowired
    PisoRepository pisoRepository;

    @Autowired 
    VehiculoRepository vehiculoRepository;

    
    @Autowired
    TipoVehiculoRepository tipoVehiculoRepository ;

    @GetMapping("/recu")
    public List<PisoEntity> getAllPisos () {
        List<PisoEntity> pisos = pisoRepository.findAll();
        return pisos;
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
    public String createPiso(@RequestParam("area") String area, @RequestParam("tipoVehiculo") String tipoVehiculo, @RequestParam("areaPorVehiculo") String areaPorVehiculo, RedirectAttributes redirectAttributes) {
        // Verificar si el tipo de vehículo existe en la base de datos
        TipoVehiculoEntity tipo = tipoVehiculoRepository.findByTipo(tipoVehiculo).orElse(null);
    
        if (tipo == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el tipo de vehículo en la base de datos. No se puede crear el piso.");
        } else {
            PisoEntity piso = new PisoEntity(area, tipo);
    
            int areaPorVehiculoInt = Integer.parseInt(areaPorVehiculo);
            int areaInt = Integer.parseInt(area);
            // Perform the multiplication
            piso.setCapacidad(areaInt / areaPorVehiculoInt);
            pisoRepository.save(piso);
            redirectAttributes.addFlashAttribute("exito", "Piso añadido exitosamente.");
        }
    
        return "redirect:/pisos/anadirPiso";
    }
    

    // Vista Para añadir un piso.
    @GetMapping("/anadirPiso")
    public String showMenu(Model model){
        return "crearPiso";
    }

    //Actualizar piso
    @PostMapping("/actu")
    public String updatePiso(
        @RequestParam("id") Long id,
        @RequestParam("area") String area,
        @RequestParam("tipoVehiculo") String tipoVehiculo,
        @RequestParam("areaPorVehiculo") String areaPorVehiculo,
        RedirectAttributes redirectAttributes
    ) {
        PisoEntity piso = pisoRepository.findById(id).orElse(null);
        
        if (piso != null) {
            // Verificar si el tipo de vehículo existe en la base de datos
            TipoVehiculoEntity tipo = tipoVehiculoRepository.findByTipo(tipoVehiculo).orElse(null);
    
            if (tipo != null) {
                piso.setArea(area);
                piso.setTipoVehiculo(tipo);
                int areaPorVehiculoInt = Integer.parseInt(areaPorVehiculo);
                int areaInt = Integer.parseInt(area);
                // Perform the multiplication
                piso.setCapacidad(areaInt / areaPorVehiculoInt);
                pisoRepository.save(piso);
                redirectAttributes.addFlashAttribute("exito", "Piso actualizado exitosamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se encontró el tipo de vehículo en la base de datos. No se puede actualizar el piso.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Error al encontrar el piso.");
        }
    
        return "redirect:/pisos/actualizarPiso";
    }
    

    // Vista para actualizar un piso.
    @GetMapping("/actualizarPiso")
    public String actualizarPiso(Model model){
        return "actualizarPiso";
    }

    /*
    @PutMapping("/{id}")
    public PisoEntity updatePiso(@PathVariable Long id, @RequestBody PisoEntity piso) {
        piso.setId(id);
        return pisoRepository.save(piso);
    }
    */

    /* 
    @DeleteMapping("/{id}")
    public void deletePiso(@PathVariable Long id) {
        pisoRepository.deleteById(id);
    }*/


    @PostMapping("/delPiso")
    public String deletePiso( @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {

        List<VehiculoEntity> vehiculos = vehiculoRepository.findAll();
        int cont = 0 ; 

        for(VehiculoEntity vehiculo : vehiculos){
            if(vehiculo.getPiso().getId().equals(id)){
                cont++;
            }
        }

        if(cont!=0){
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el piso, el piso tiene vehiculos parquedaos.");
        }else{
            pisoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("error", "Piso eliminado correctamente");
        }
        return  "redirect:/pisos/borrarPiso";
    }


    // Vista para actualizar un piso.
    @GetMapping("/borrarPiso")
    public String borrarPiso(Model model){
        return "eliminarPiso";
    }

}
