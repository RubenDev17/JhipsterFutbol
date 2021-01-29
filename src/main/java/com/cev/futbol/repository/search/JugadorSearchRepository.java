package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Jugador;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Jugador} entity.
 */
public interface JugadorSearchRepository extends ElasticsearchRepository<Jugador, Long> {
}
