package com.adminParking.adminParking.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tabla_piso")
public class PisoEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String area;

    @OneToMany(mappedBy = "piso")
    private List<VehiculoEntity> vehiculos;

      public PisoEntity() {
        // Constructor vacío necesario para JPA
    }

    public PisoEntity(String area) {
        this.area = area;
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

    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoEntity> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
