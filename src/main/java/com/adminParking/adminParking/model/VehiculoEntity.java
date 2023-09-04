package com.adminParking.adminParking.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class VehiculoEntity {

    @Id
    @GeneratedValue
    Long id;

    String tiempoLlegada; 
    String tiempoSalida;

    public VehiculoEntity(Long id, String tiempoLlegada, String tiempoSalida) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoSalida = tiempoSalida;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTiempoLlegada(String tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public void setTiempoSalida(String tiempoSalida) {
        this.tiempoSalida = tiempoSalida;
    }


    public Long getId() {
        return id;
    }

    public String getTiempoLlegada() {
        return tiempoLlegada;
    }

    public String getTiempoSalida() {
        return tiempoSalida;
    }
}
