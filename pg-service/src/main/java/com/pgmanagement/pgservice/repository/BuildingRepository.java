package com.pgmanagement.pgservice.repository;

import com.pgmanagement.pgservice.model.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    Optional<Building> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);
}