package com.adminParking.adminParking.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tabla_tarifa")
public class TarifaEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String tipoVehiculo;
    private double tarifaPorMinuto;

    public TarifaEntity(){
        
    }
 
    public TarifaEntity(String tipoVehiculo, double tarifaPorMinuto) {
        this.tipoVehiculo = tipoVehiculo;
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTarifaPorMinuto(double tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }


    public Long getId() {
        return id;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }
    
}
