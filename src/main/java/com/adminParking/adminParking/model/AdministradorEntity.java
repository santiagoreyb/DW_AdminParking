package com.adminParking.adminParking.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.adminParking.adminParking.service.CapacidadVehiculosService;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class AdministradorEntity {

    @Id
    private Long id;

    //Lista de pisos
    @OneToMany(mappedBy = "administrador")
    private List<PisoEntity> pisos;

    public AdministradorEntity() {
        // Constructor vacío necesario para JPA
    }

    public AdministradorEntity(Long id) {
        this.id = id;
    }

    // métodos y lógica relacionada con el administrador aquí,  cálculo de espacios disponibles
    public int calcularEspaciosDisponibles(Long id) {
         
        PisoEntity pisoDef =null; 

        // Verifica si los pisos se están trayendo correctamente
        if (pisos != null) {
            for (PisoEntity piso : pisos) {
                // Realiza alguna operación para verificar cada piso, por ejemplo, imprimir su ID
                if(piso.getId().equals(id)){
                    pisoDef =piso;
                }
                //System.out.println("Piso ID: " + piso.getVehiculos());
            } 
        }


        if (pisoDef == null) {
            System.out.println("No se encontro el piso para calcular el espacio disponible");
            return -1; 
        }


        /* Placas de los vehiculos
        List<VehiculoEntity> vehiculosEstacionadoss = pisoDef.getVehiculos();
    
        System.out.println("Placas de los vehículos en este piso:");
        for (VehiculoEntity vehiculo : vehiculosEstacionadoss) {
            System.out.println("Placa: " + vehiculo.getPlaca());
        }*/

        int capacidadTotal = obtenerCapacidadTotalPorTipoDeVehiculo(pisoDef);

        // Obttiene la cantidad actual de vehículos estacionados en este piso
        int vehiculosEstacionados = pisoDef.getVehiculos().size();

       // System.out.println(vehiculosEstacionados);

        // Calcular los espacios disponibles restando los vehículos estacionados de la capacidad total
        int espaciosDisponibles = capacidadTotal - vehiculosEstacionados;

        // resultado no sea negativo
        if (espaciosDisponibles < 0) {
            espaciosDisponibles = 0;
        }
        
        return espaciosDisponibles;
    }

    private int obtenerCapacidadTotalPorTipoDeVehiculo(PisoEntity piso) {

        String tipoVehiculo = piso.getTipoVehiculo();
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