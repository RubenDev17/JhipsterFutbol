package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Dato;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Dato} entity.
 */
public interface DatoSearchRepository extends ElasticsearchRepository<Dato, Long> {
}
