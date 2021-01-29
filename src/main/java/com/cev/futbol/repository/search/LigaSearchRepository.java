package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Liga;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Liga} entity.
 */
public interface LigaSearchRepository extends ElasticsearchRepository<Liga, Long> {
}
