package com.sansoft.inscription.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Scolarite.
 */
@Entity
@Table(name = "scolarite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "scolarite")
public class Scolarite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "effectif", nullable = false)
    private Integer effectif;

    @NotNull
    @Column(name = "scolarite", nullable = false)
    private Float scolarite;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "scolarites", allowSetters = true)
    private Niveau niveau;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "scolarites", allowSetters = true)
    private AnneeScolaire anneeScolaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEffectif() {
        return effectif;
    }

    public Scolarite effectif(Integer effectif) {
        this.effectif = effectif;
        return this;
    }

    public void setEffectif(Integer effectif) {
        this.effectif = effectif;
    }

    public Float getScolarite() {
        return scolarite;
    }

    public Scolarite scolarite(Float scolarite) {
        this.scolarite = scolarite;
        return this;
    }

    public void setScolarite(Float scolarite) {
        this.scolarite = scolarite;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public Scolarite niveau(Niveau niveau) {
        this.niveau = niveau;
        return this;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public AnneeScolaire getAnneeScolaire() {
        return anneeScolaire;
    }

    public Scolarite anneeScolaire(AnneeScolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
        return this;
    }

    public void setAnneeScolaire(AnneeScolaire anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Scolarite)) {
            return false;
        }
        return id != null && id.equals(((Scolarite) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Scolarite{" +
            "id=" + getId() +
            ", effectif=" + getEffectif() +
            ", scolarite=" + getScolarite() +
            "}";
    }
}
