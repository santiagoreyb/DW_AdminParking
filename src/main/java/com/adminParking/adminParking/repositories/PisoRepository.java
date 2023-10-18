package com.adminParking.adminParking.repositories;

import com.adminParking.adminParking.model.PisoEntity;
import com.adminParking.adminParking.model.TipoVehiculoEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PisoRepository extends JpaRepository<PisoEntity, Long> {
    List<PisoEntity> findByTipoVehiculo(TipoVehiculoEntity tipoVehiculo);

}
