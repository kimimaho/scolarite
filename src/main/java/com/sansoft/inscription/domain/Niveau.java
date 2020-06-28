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
 * A Niveau.
 */
@Entity
@Table(name = "niveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "niveau")
public class Niveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "intitule", nullable = false)
    private String intitule;

    @OneToMany(mappedBy = "niveau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Inscription> inscriptions = new HashSet<>();

    @OneToMany(mappedBy = "niveau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Scolarite> scolarites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public Niveau intitule(String intitule) {
        this.intitule = intitule;
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Set<Inscription> getInscriptions() {
        return inscriptions;
    }

    public Niveau inscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
        return this;
    }

    public Niveau addInscriptions(Inscription inscription) {
        this.inscriptions.add(inscription);
        inscription.setNiveau(this);
        return this;
    }

    public Niveau removeInscriptions(Inscription inscription) {
        this.inscriptions.remove(inscription);
        inscription.setNiveau(null);
        return this;
    }

    public void setInscriptions(Set<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public Set<Scolarite> getScolarites() {
        return scolarites;
    }

    public Niveau scolarites(Set<Scolarite> scolarites) {
        this.scolarites = scolarites;
        return this;
    }

    public Niveau addScolarite(Scolarite scolarite) {
        this.scolarites.add(scolarite);
        scolarite.setNiveau(this);
        return this;
    }

    public Niveau removeScolarite(Scolarite scolarite) {
        this.scolarites.remove(scolarite);
        scolarite.setNiveau(null);
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
        if (!(o instanceof Niveau)) {
            return false;
        }
        return id != null && id.equals(((Niveau) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Niveau{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            "}";
    }
}
