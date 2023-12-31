package com.adminParking.adminParking.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminParking.adminParking.model.TarifaEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;

@Repository
public interface TarifaRepository extends JpaRepository<TarifaEntity, Long> {
    Optional<TarifaEntity> findByTipoVehiculo(TipoVehiculoEntity tipoVehiculo);
}
