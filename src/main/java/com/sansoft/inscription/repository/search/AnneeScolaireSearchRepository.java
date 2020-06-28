package com.sansoft.inscription.repository.search;

import com.sansoft.inscription.domain.AnneeScolaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link AnneeScolaire} entity.
 */
public interface AnneeScolaireSearchRepository extends ElasticsearchRepository<AnneeScolaire, Long> {
}
