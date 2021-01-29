package com.cev.futbol.repository;

import com.cev.futbol.domain.Dato;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dato entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DatoRepository extends JpaRepository<Dato, Long> {
}
