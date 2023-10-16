package com.adminParking.adminParking.service;


import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class CapacidadVehiculosService {

    private Map<String, Integer> capacidadPorTipoYArea = new HashMap<>();

    // public CapacidadVehiculosService() {
    //     capacidadPorTipoYArea.put("Carro", 2); // 2 automóviles por metro cuadrado
    //     capacidadPorTipoYArea.put("Moto", 3); // 3 metros por metro cuadrado
    //     capacidadPorTipoYArea.put("Camión", 1);   // 1 camión por metro cuadrado
    //     capacidadPorTipoYArea.put("Scooter", 4);   // 5 Scooters por metro cuadrado
    // }

    public int obtenerCapacidadPorTipoYArea(String tipoVehiculo) {
        return capacidadPorTipoYArea.getOrDefault(tipoVehiculo, 0);
    }
}