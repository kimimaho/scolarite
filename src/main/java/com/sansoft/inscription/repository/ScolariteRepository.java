package com.sansoft.inscription.repository;

import com.sansoft.inscription.domain.Scolarite;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Scolarite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScolariteRepository extends JpaRepository<Scolarite, Long> {
}
