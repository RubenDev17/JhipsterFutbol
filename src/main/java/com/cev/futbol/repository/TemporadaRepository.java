package com.cev.futbol.repository;

import com.cev.futbol.domain.Temporada;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Temporada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemporadaRepository extends JpaRepository<Temporada, Long> {
}
