package com.cev.futbol.repository;

import com.cev.futbol.domain.Liga;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Liga entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigaRepository extends JpaRepository<Liga, Long> {
}
