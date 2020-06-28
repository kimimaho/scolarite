package com.sansoft.inscription.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link NiveauSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class NiveauSearchRepositoryMockConfiguration {

    @MockBean
    private NiveauSearchRepository mockNiveauSearchRepository;

}
