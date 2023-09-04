package com.adminParking.adminParking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminParking.adminParking.model.VehiculoEntity;

@Repository
public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Long> {
    
}
