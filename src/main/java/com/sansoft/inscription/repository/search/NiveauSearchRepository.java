package com.sansoft.inscription.repository.search;

import com.sansoft.inscription.domain.Niveau;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Niveau} entity.
 */
public interface NiveauSearchRepository extends ElasticsearchRepository<Niveau, Long> {
}
