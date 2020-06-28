package com.sansoft.inscription.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EleveSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EleveSearchRepositoryMockConfiguration {

    @MockBean
    private EleveSearchRepository mockEleveSearchRepository;

}
