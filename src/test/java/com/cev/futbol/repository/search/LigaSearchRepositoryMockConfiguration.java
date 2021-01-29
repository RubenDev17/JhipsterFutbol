package com.cev.futbol.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link LigaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LigaSearchRepositoryMockConfiguration {

    @MockBean
    private LigaSearchRepository mockLigaSearchRepository;

}
