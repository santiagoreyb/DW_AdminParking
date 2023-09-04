package com.adminParking.adminParking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminParking.adminParking.model.AdministradorEntity;

@Repository
public interface AdministradorRepository extends JpaRepository<AdministradorEntity, Long> {
}

