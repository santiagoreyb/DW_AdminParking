package com.adminParking.adminParking.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tabla_piso")
public class PisoEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String area;
    private String tipoVehiculo; 

    @OneToMany(mappedBy = "piso") //"piso" nombre del atributo de la otra clase que conforma esta asociaicon
    @JsonIgnore // Ignore the vehiculos field during JSON serialization
    private List<VehiculoEntity> vehiculos = new ArrayList<>();

    @ManyToOne
    private AdministradorEntity administrador; //"administrador" nombre del atributo de la otra clase que conforma esta asociaicon

      public PisoEntity() {
        // Constructor vacío necesario para JPA
    }

    public PisoEntity(String area, String tipoVehiculo) {
        this.area = area;
        this.tipoVehiculo = tipoVehiculo;
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

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setAdministrador(AdministradorEntity administrador) {
        this.administrador = administrador;
    }


    public AdministradorEntity getAdministrador() {
        return administrador;
    }

    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoEntity> vehiculos) {
        this.vehiculos = vehiculos;
    }


}
