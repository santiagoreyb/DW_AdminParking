package com.adminParking.adminParking.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.adminParking.adminParking.model.Cuenta;
import com.adminParking.adminParking.repositories.CuentaRepository;

@Service
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;

    public BigDecimal retirar(@PathVariable Long id, @RequestBody BigDecimal cantidad) {
        Cuenta cuenta = cuentaRepository.findById(id).orElseThrow();

        BigDecimal saldo = cuenta.getSaldo();
        saldo = saldo.subtract(cantidad);

        cuenta.setSaldo(saldo);
        cuentaRepository.save(cuenta);
        return cuenta.getSaldo();
    }

    public BigDecimal abonar(@PathVariable Long id, @RequestBody BigDecimal cantidad) {
        Cuenta cuenta = cuentaRepository.findById(id).orElseThrow();

        BigDecimal saldo = cuenta.getSaldo();
        saldo = saldo.add(cantidad);

        cuenta.setSaldo(saldo);
        cuentaRepository.save(cuenta);
        return cuenta.getSaldo();
    }

    public BigDecimal saldo(@PathVariable Long id) {
        return cuentaRepository.findById(id).orElseThrow().getSaldo();
    }

    public Cuenta cuenta(@PathVariable Long id) {
        return cuentaRepository.findById(id).orElseThrow();

    }

    public List<Cuenta> all() {
        return cuentaRepository.findAll();

    }

}