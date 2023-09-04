package com.adminParking.adminParking.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "tabla_vehiculo")
public class VehiculoEntity {

    @Id
    @GeneratedValue
    Long id;

    String tiempoLlegada; 
    String tiempoSalida;

    @ManyToOne //Se utiliza para indicar que un vehículo pertenece a un piso 
    @JoinColumn(name = "piso_id") //se usa para especificar la columna en la tabla de vehículos que almacena la clave foránea al piso
    private PisoEntity piso; // Agrega la referencia al piso

    public VehiculoEntity() {
        // Constructor vacío necesario para JPA
    }

    public VehiculoEntity(String tiempoLlegada, String tiempoSalida) {
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

    public PisoEntity getPiso() {
        return piso;
    }

    public void setPiso(PisoEntity piso) {
        this.piso = piso;
    }
}
