package com.cev.futbol.repository;

import com.cev.futbol.domain.Jugador;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Jugador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}
