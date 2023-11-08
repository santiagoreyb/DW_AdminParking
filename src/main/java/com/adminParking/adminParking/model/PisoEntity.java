package com.adminParking.adminParking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "piso")
public class PisoEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String area;
    
    @ManyToOne
    private TipoVehiculoEntity tipoVehiculo; 

    private int capacidad; 

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @OneToMany(mappedBy = "piso") //"piso" nombre del atributo de la otra clase que conforma esta asociaicon
    @JsonIgnore // Ignore the vehiculos field during JSON serialization
    private List<VehiculoEntity> vehiculos = new ArrayList<>();


    public PisoEntity() {
        // Constructor vacío necesario para JPA
    }

    public PisoEntity(String area, TipoVehiculoEntity tipoVehiculo) {
        this.area = area;
        this.tipoVehiculo = tipoVehiculo;
    }

    public PisoEntity(String area, TipoVehiculoEntity tipoVehiculo, int Capacidad) {
        this.area = area;
        this.tipoVehiculo = tipoVehiculo;
        this.capacidad = Capacidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setTipoVehiculo(TipoVehiculoEntity tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public TipoVehiculoEntity getTipoVehiculo() {
        return tipoVehiculo;
    }

    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoEntity> vehiculos) {
        this.vehiculos = vehiculos;
    }


}
