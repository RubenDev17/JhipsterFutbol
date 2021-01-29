package com.cev.futbol.repository;

import com.cev.futbol.domain.Presidente;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Presidente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresidenteRepository extends JpaRepository<Presidente, Long> {
}
