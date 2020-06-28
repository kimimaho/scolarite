package com.sansoft.inscription.repository;

import com.sansoft.inscription.domain.AnneeScolaire;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnneeScolaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire, Long> {
}
