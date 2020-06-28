package com.sansoft.inscription.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AnneeScolaire.
 */
@Entity
@Table(name = "annee_scolaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "anneescolaire")
public class AnneeScolaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "annee", nullable = false)
    private Integer annee;

    @OneToMany(mappedBy = "anneeScolaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Inscription> inscriptions = new HashSet<>();

    @OneToMany(mappedBy = "anneeScolaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Scolarite> scolarites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnnee() {
        return annee;
    }

    public AnneeScolaire annee(Integer annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public AnneeScolaire inscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
        return this;
    }

    public AnneeScolaire addInscriptions(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setAnneeScolaire(this);
        return this;
    }

    public AnneeScolaire removeInscriptions(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setAnneeScolaire(null);
        return this;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public Set<Scolarite> getScolarites() {
        return scolarites;
    }

    public AnneeScolaire scolarites(Set<Scolarite> scolarites) {
        this.scolarites = scolarites;
        return this;
    }

    public AnneeScolaire addScolarite(Scolarite scolarite) {
        this.scolarites.add(scolarite);
        scolarite.setAnneeScolaire(this);
        return this;
    }

    public AnneeScolaire removeScolarite(Scolarite scolarite) {
        this.scolarites.remove(scolarite);
        scolarite.setAnneeScolaire(null);
        return this;
    }

    public void setScolarites(Set<Scolarite> scolarites) {
        this.scolarites = scolarites;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeScolaire)) {
            return false;
        }
        return id != null && id.equals(((AnneeScolaire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeScolaire{" +
            "id=" + getId() +
            ", annee=" + getAnnee() +
            "}";
    }
}
