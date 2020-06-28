package com.sansoft.inscription.repository.search;

import com.sansoft.inscription.domain.Eleve;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Eleve} entity.
 */
public interface EleveSearchRepository extends ElasticsearchRepository<Eleve, Long> {
}
