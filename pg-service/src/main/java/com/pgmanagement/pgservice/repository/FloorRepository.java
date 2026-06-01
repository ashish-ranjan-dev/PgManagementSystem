package com.pgmanagement.pgservice.repository;

import com.pgmanagement.pgservice.model.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
}