package com.cev.futbol.repository;

import com.cev.futbol.domain.Partido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Partido entity.
 */
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    @Query(value = "select distinct partido from Partido partido left join fetch partido.jugadors",
        countQuery = "select count(distinct partido) from Partido partido")
    Page<Partido> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct partido from Partido partido left join fetch partido.jugadors")
    List<Partido> findAllWithEagerRelationships();

    @Query("select partido from Partido partido left join fetch partido.jugadors where partido.id =:id")
    Optional<Partido> findOneWithEagerRelationships(@Param("id") Long id);
}
