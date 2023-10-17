package com.adminParking.adminParking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifa")
public class TarifaEntity {
    @Id
    @GeneratedValue
    private Long id;
    private double tarifaPorMinuto;

    @ManyToOne
    private TipoVehiculoEntity tipoVehiculo;

    public TarifaEntity(){
        
    }
 
    public TarifaEntity(TipoVehiculoEntity tipoVehiculo, double tarifaPorMinuto) {
        this.tipoVehiculo = tipoVehiculo;
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTarifaPorMinuto(double tarifaPorMinuto) {
        this.tarifaPorMinuto = tarifaPorMinuto;
    }

    public void setTipoVehiculo(TipoVehiculoEntity tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }


    public Long getId() {
        return id;
    }

    public double getTarifaPorMinuto() {
        return tarifaPorMinuto;
    }

    public TipoVehiculoEntity getTipoVehiculo() {
        return tipoVehiculo;
    }
    
}
