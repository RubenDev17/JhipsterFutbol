package com.cev.futbol.repository;

import com.cev.futbol.domain.Partido;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Partido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {
}
