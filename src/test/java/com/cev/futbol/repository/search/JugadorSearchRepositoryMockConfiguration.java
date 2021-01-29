package com.cev.futbol.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link JugadorSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class JugadorSearchRepositoryMockConfiguration {

    @MockBean
    private JugadorSearchRepository mockJugadorSearchRepository;

}
