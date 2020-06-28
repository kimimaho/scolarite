package com.sansoft.inscription.repository.search;

import com.sansoft.inscription.domain.Inscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Inscription} entity.
 */
public interface InscriptionSearchRepository extends ElasticsearchRepository<Inscription, Long> {
}
