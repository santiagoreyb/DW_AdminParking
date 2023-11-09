package com.adminParking.adminParking.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class Cuenta {
    @Id
    @GeneratedValue
    Long id;

    String name;

    @Column(name = "SALDO", precision = 20, scale = 2)
    BigDecimal saldo;

    Cuenta() {

    }

    public Cuenta(String name, String saldo) {
        this.name = name;
        this.saldo = new BigDecimal(saldo);
    }

    public Cuenta(Long id, String name, BigDecimal saldo) {
        this.id = id;
        this.name = name;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

}
