package com.sansoft.inscription.repository.search;

import com.sansoft.inscription.domain.Scolarite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Scolarite} entity.
 */
public interface ScolariteSearchRepository extends ElasticsearchRepository<Scolarite, Long> {
}
