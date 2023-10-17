package com.adminParking.adminParking.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_vehiculo")
public class TipoVehiculoEntity {

    @Id
    @GeneratedValue
    Long id;

    private String tipo;

    public TipoVehiculoEntity() { }

    public TipoVehiculoEntity(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
