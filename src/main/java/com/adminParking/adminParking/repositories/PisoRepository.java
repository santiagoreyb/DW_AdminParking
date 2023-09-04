package com.adminParking.adminParking.repositories;

import com.adminParking.adminParking.model.PisoEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PisoRepository extends JpaRepository<PisoEntity, Long> {
}
